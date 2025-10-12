package com.srap.wash.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.srap.wash.utils.StorageUtil
import com.srap.wash.utils.ToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun sendSmsCode(navController: NavController, viewModel: LoginViewModel, phone: String) {
    viewModel.sendSmsCode(phone) {
        navController.navigate("login/code/$phone")
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // 加载状态
    var timerText by remember { mutableStateOf("60秒") }
    val viewModel: LoginViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope() // 获取协程作用域

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 标题部分
        Column(
            modifier = Modifier
                .weight(2f) // 占据上半部分
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // 垂直居中
        ) {
            Text(
                text = "登录",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 32.dp) // 添加顶部间距
            )
        }

        // 输入框部分
        Column(
            modifier = Modifier
                .weight(3f) // 占据下半部分
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // 添加左右边距
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = phone,
                onValueChange = { value -> phone = value },
                label = { Text("请输入手机号") }
            )

            Button(
                modifier = Modifier.padding(vertical = 16.dp),
                onClick = {
                    if (isLoading) return@Button
                    isLoading = true
                    timerText = "60秒"
                    sendSmsCode(navController, viewModel, phone)

                    // 启动协程进行倒计时
                    coroutineScope.launch {
                        for (i in 60 downTo 1) {
                            timerText = "${i}秒"
                            delay(1000)
                        }
                        isLoading = false
                    }
                }
            ) {
                Text(if (isLoading) timerText else "登录")
            }
        }
    }
}