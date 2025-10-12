package com.srap.wash.ui.washlist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.LatestUsedResponse
import com.srap.wash.logic.model.MessageListResponse
import com.srap.wash.logic.model.OrderDetailResponse
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.Code
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.ui.base.BaseViewModel
import com.srap.wash.utils.StorageUtil
import com.srap.wash.utils.TimeUtil
import com.srap.wash.utils.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimeData(
    var timeStamp: Long,
    val allTimeStamp: Long
)

@HiltViewModel
class WashListViewModel @Inject constructor(
    networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {
    private val TAG = javaClass.simpleName

    var data by mutableStateOf<List<LatestUsedResponse.Data>>(emptyList())
        private set

    var messageData = mutableStateMapOf<Int, TimeData>()
        private set

    var loading by mutableStateOf(false)
        private set

    init {
        fetchWashList()
    }

    private suspend fun getOrderDetail(orderId: String): OrderDetailResponse.Data? {
        return when (val state = networkRepo.getOrderDetail(orderId).first()) {
            is LoadingState.Error -> {
                null
            }

            is LoadingState.Success -> {
                state.response
            }
        }
    }

    private fun fetchWashList() {
        viewModelScope.launch {
            loading = true
            networkRepo.getLatestUsed("001")
                .collect { state ->
                    when (state) {
                        is LoadingState.Error -> {
                            loading = false
                            ToastUtil.show("[$TAG] ${state.errMsg}")
                        }
                        is LoadingState.Success -> {
                            loading = false
                            val goodsList = state.response
                            if (!goodsList.isNullOrEmpty()) {
                                data = goodsList
                                return@collect
                            }
                            ToastUtil.show("[$TAG] 数据异常")
                        }
                    }
                }
            networkRepo.getMessageList()
                .collect { state ->
                    when (state) {
                        is LoadingState.Error -> {
                            loading = false
                            ToastUtil.show("[$TAG] ${state.errMsg}")
                        }
                        is LoadingState.Success -> {
                            loading = false
                            val goodsList = state.response
                            if (goodsList != null) {
                                goodsList.forEach { goods ->
                                    if (goods.order == null) return@forEach
                                    val orderId = goods.order.orderNo
                                    val orderDetail = getOrderDetail(orderId)
                                    Log.i("TAG", "fetchWashList: $orderDetail")
                                    if (orderDetail?.completeTime != null) {
                                        messageData[orderDetail.lineItems.first().goodsId] = TimeData(
                                            timeStamp = TimeUtil.getTimeDifferenceWithCurrent(orderDetail.completeTime),
                                            allTimeStamp = TimeUtil.calculateTimeDifference(
                                                createTimeStr = orderDetail.createTime,
                                                finishTimeStr = orderDetail.completeTime
                                            ),
                                        )
                                    }
                                }
                                return@collect
                            }
                            ToastUtil.show("[$TAG] 数据异常")
                        }
                    }
                }
        }
    }

    suspend fun isLogin(): Boolean {
        val token = StorageUtil.Token
        if (token.isEmpty()) return false
        val loginStatus = networkRepo.isLoginStatus()
        return loginStatus.code != Code.NOT_LOGIN
    }
}