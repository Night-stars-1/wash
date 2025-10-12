package com.srap.wash.ui.goodsdetail

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alipay.sdk.app.PayTask
import com.google.gson.Gson
import com.srap.wash.logic.model.GoodsDetailsResponse
import com.srap.wash.logic.model.GoodsSkusResponse
import com.srap.wash.logic.model.PayResult
import com.srap.wash.utils.ToastUtil
import kotlinx.coroutines.delay


fun pay(activity: Activity?, payModel: PayViewModel, orgId: Int, goodsId: Int, skuId: Int) {
    val promotions = listOf(
        mapOf(
            "assetId" to "0",
            "oldPromotionId" to "3482",
            "orgId" to orgId.toString(),
            "promotionId" to "0",
            "promotionType" to "4"
        )
    )
    val items = listOf(
        mapOf(
            "amount" to "1",
            "goodsId" to goodsId.toString(),
            "num" to "1",
            "skuId" to skuId.toString(),
            "soldType" to "1"
        )
    )
    val handler = Handler(Looper.getMainLooper()) { msg ->
        // pay: 1 {resultStatus=9000, result={"alipay_trade_app_pay_response":{"code":"10000","msg":"Success","app_id":"","auth_app_id":"","charset":"UTF-8","timestamp":"2024-11-05 12:23:29","out_trade_no":"","total_amount":"3.00","trade_no":"","seller_id":""},"sign":"","sign_type":"RSA2"}, memo=, extendInfo={"doNotExit":true,"isDisplayResult":true,"tradeNo":""}}
        Log.i("Pay", "pay: ${msg.what} ${msg.obj}")
        if (msg.what == 1) {
            val gson = Gson()
            val payResult = gson.fromJson(gson.toJson(msg.obj), PayResult::class.java)
            if (payResult.resultStatus == 9000) {
                ToastUtil.show("支付成功")
            } else {
                ToastUtil.show("支付失败")
            }
        }
        return@Handler false
    }

    payModel.createTrade(promotions, items) {
        if (it == null) return@createTrade
        payModel.prePay(orderNo = it.orderId, payType = 13) { payIt ->
            if (payIt == null) return@prePay
            val payRunnable = Runnable {
                val alipay = PayTask(activity)
                val result = alipay.payV2(payIt.prepayParam, true)
                val msg = Message()
                msg.what = 1
                msg.obj = result
                handler.sendMessage(msg)
            }

            // 必须异步调用
            val payThread = Thread(payRunnable)
            payThread.start()
        }
    }
}

@Composable
fun DetailWidget(data: GoodsDetailsResponse.Data) {
    val context = LocalContext.current
    val activity = context as? Activity
    val items = hiltViewModel<GoodsSkusViewModel, GoodsSkusViewModel.ViewModelFactory> { factory ->
        factory.create(goodsId = data.goodsId)
    }
    val payModel: PayViewModel = hiltViewModel()
    val animatedItems = remember { mutableStateListOf<GoodsSkusResponse.Data>() }

    LaunchedEffect(items.data) {
        animatedItems.clear()
        for (item in items.data) {
            animatedItems.add(item)
            // 控制渐出的卡片间隔
            delay(200)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = data.orgName,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        LazyColumn {
            items(animatedItems) { item ->
                val isVisible = remember { mutableStateOf(false) }
                AnimatedVisibility(
                    visible = isVisible.value,
                    enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 1000)),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        onClick = {
                            pay(
                                activity = activity,
                                payModel = payModel,
                                orgId = data.orgId,
                                goodsId = data.goodsId,
                                skuId = item.skuId
                            )
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween // 左右分隔
                        ) {
                            // 左边的两行文字
                            Column {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = item.feature,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            // 右边的文字，居中对齐
                            Text(
                                text = "￥ ${item.price}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterVertically) // 垂直居中
                            )
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    isVisible.value = true
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, goodsId: Int) {
    val viewModel = hiltViewModel<GoodsDetailViewModel, GoodsDetailViewModel.ViewModelFactory> { factory ->
        factory.create(goodsId = goodsId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("洗衣机详细")
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val data = viewModel.data
            // 显示加载动画
            if (data == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                // 显示请求结果
                DetailWidget(data)
            }
        }
    }
}