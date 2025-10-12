package com.srap.wash.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.srap.wash.ui.goodsdetail.DetailScreen
import com.srap.wash.ui.login.LoginScreen
import com.srap.wash.ui.login.code.CodeScreen
import com.srap.wash.ui.main.MainScreen
import com.srap.wash.ui.scancode.ScanCodeScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            MainScreen(
                navController = navController
            )
        }

        composable(
            "detail/{goodsId}",
            arguments = listOf(
                navArgument("goodsId") {
                    type = NavType.IntType
                },
            ),
        ) {
            val goodsId = it.arguments?.getInt("goodsId")
            if (goodsId != null) {
                DetailScreen(navController, goodsId)
            } else {
                Text("商品ID异常")
            }
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable(
            "login/code/{phone}",
            arguments = listOf(
                navArgument("phone") {
                    type = NavType.StringType
                },
            )
        ) {
            val phone = it.arguments?.getString("phone")
            if (phone != null) {
                CodeScreen(navController, phone)
            } else {
                Text("手机号异常")
            }
        }

        composable("scan/code") {
            ScanCodeScreen(navController)
        }

    }
}

