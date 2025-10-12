package com.srap.wash.ui.scancode

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.srap.wash.utils.ToastUtil
import androidx.core.net.toUri

@Composable
fun ScanCodeScreen(navController: NavController) {
    val viewModel: ScanCodeViewModel = hiltViewModel()

    fun pageTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        val url = result.contents
        if (url == null) {
            pageTo("home")
            return@rememberLauncherForActivityResult
        }
        val uri = url.toUri()
        val nqt = uri.getQueryParameter("NQT")
        if (nqt != null) {
            viewModel.goodsScan(nqt, {
                ToastUtil.show("无效设备")
                pageTo("home")
            }) {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo("home", false)
                    .build()
                navController.navigate("detail/$it", navOptions)
            }
        } else {
            ToastUtil.show("无效二维码")
            pageTo("home")
        }
    }

    val options = ScanOptions().apply {
        setDesiredBarcodeFormats(ScanOptions.QR_CODE) // 仅扫描二维码
        setPrompt("请将二维码移入取景框")
        setOrientationLocked(false)
        setBeepEnabled(true)
    }

    // 使用 LaunchedEffect 在组件启动时自动启动扫描
    LaunchedEffect(Unit) {
        barcodeLauncher.launch(options)
    }
}
