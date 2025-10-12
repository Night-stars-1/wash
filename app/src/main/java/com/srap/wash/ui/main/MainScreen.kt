package com.srap.wash.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.srap.wash.ui.order.OrderScreen
import com.srap.wash.ui.washlist.WashListScreen

@Composable
fun MainScreen(
    navController: NavController
) {
    val savableStateHolder = rememberSaveableStateHolder()
    var selectIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = selectIndex == 0,
                label = {
                    Text("主页")
                },
                onClick = {
                    selectIndex = 0
                },
                icon = {
                    Icon(Icons.Filled.Home, contentDescription = "主页")
                }
            )

            item(
                selected = selectIndex == 1,
                label = {
                    Text("订单")
                },
                onClick = {
                    selectIndex = 1
                },
                icon = {
                    Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = "订单")
                }
            )
        }
    ) {
        AnimatedContent(
            targetState = selectIndex,
            label = "MainScreen",
            transitionSpec = {
                if (targetState >= initialState) {
                    // 前进动画
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    // 后退动画
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }

                }
            },
        ) { index ->
            savableStateHolder.SaveableStateProvider(
                key = index,
                content = {
                    when (index) {
                        0 -> WashListScreen(navController)
                        1 -> OrderScreen(navController)
                    }
                }
            )
        }
    }
}