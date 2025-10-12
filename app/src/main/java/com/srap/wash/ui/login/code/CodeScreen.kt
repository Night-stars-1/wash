package com.srap.wash.ui.login.code

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.srap.wash.utils.StorageUtil
import com.srap.wash.utils.ToastUtil

@Composable
fun CodeScreen(navController: NavController, phone: String) {
    val viewModel: CodeViewModel = hiltViewModel()

    VerificationCodeTextField(codeLength = 4) {
        viewModel.userReg(phone = phone, code = it) { token ->
            StorageUtil.Token = token
            ToastUtil.show("登录成功")
            navController.navigate("home")
        }
    }
}