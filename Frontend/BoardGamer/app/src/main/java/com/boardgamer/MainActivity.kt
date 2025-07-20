package com.boardgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boardgamer.ui.Home
import com.boardgamer.ui.theme.BoardGamerTheme
import com.boardgamer.viewmodel.HomeViewModel
import com.boardgamer.viewmodel.RegistrationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoardGamerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeViewModel.SCREEN_NAME) {
        composable(HomeViewModel.SCREEN_NAME) {
            Home(navController = navController)
        }
        composable(RegistrationViewModel.SCREEN_NAME) {
            Registration(navController)
        }
    }
}
