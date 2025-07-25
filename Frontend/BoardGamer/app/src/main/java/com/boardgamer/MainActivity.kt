package com.boardgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boardgamer.ui.CurrentEvents
import com.boardgamer.ui.GameLibrary
import com.boardgamer.ui.Home
import com.boardgamer.ui.NewAppointment
import com.boardgamer.ui.Profile
import com.boardgamer.ui.theme.BoardGamerTheme
import com.boardgamer.viewmodel.CurrentEventsViewModel
import com.boardgamer.viewmodel.GameLibraryViewModel
import com.boardgamer.viewmodel.HomeViewModel
import com.boardgamer.viewmodel.NewAppointmentViewModel
import com.boardgamer.viewmodel.ProfileViewModel
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
            Registration(navController = navController)
        }
        composable(ProfileViewModel.SCREEN_NAME + "/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId")?.toLong()
            if (playerId != null) {
                Profile(navController = navController, playerId = playerId)
            }
        }
        composable(CurrentEventsViewModel.SCREEN_NAME + "/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId")?.toLong()
            if (playerId != null) {
                CurrentEvents(navController = navController, playerId = playerId)
            }
        }
        composable(GameLibraryViewModel.SCREEN_NAME) {
            GameLibrary(navController = navController)
        }
        composable(NewAppointmentViewModel.SCREEN_NAME + "/{hostId}") { backStackEntry ->
            val hostId = backStackEntry.arguments?.getString("hostId")?.toLong()
            if (hostId != null) {
                NewAppointment(navController = navController, hostId = hostId)
            }
        }
        composable(ProfileViewModel.SCREEN_NAME + "/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId")

            if (playerId != null) {
                Profile(navController, playerId.toLong())
            }
        }
    }
}
