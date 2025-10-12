package com.srap.wash

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.srap.wash.ui.AppNavigation
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.srap.wash.ui.component.PermissionDeniedDialog
import com.srap.wash.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            navController = rememberNavController()


            AppTheme(
                dynamicColor = true
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
//                .windowInsetsPadding(ScaffoldDefaults.contentWindowInsets)
                ) {
                    PermissionDeniedDialog()
                    AppNavigation(navController)
                }
            }
        }
    }
}
