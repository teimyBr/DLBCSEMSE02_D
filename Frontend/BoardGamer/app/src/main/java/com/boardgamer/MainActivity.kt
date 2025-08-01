package com.boardgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boardgamer.ui.AppointmentInfos
import com.boardgamer.ui.CurrentEvents
import com.boardgamer.ui.GameLibrary
import com.boardgamer.ui.Home
import com.boardgamer.ui.NewAppointment
import com.boardgamer.ui.NewMessage
import com.boardgamer.ui.Participate
import com.boardgamer.ui.Profile
import com.boardgamer.ui.Registration
import com.boardgamer.ui.SuggestGame
import com.boardgamer.ui.theme.BoardGamerTheme
import com.boardgamer.viewmodel.AppointmentInfosViewModel
import com.boardgamer.viewmodel.CurrentEventsViewModel
import com.boardgamer.viewmodel.GameLibraryViewModel
import com.boardgamer.viewmodel.HomeViewModel
import com.boardgamer.viewmodel.NewAppointmentViewModel
import com.boardgamer.viewmodel.NewMessageViewModel
import com.boardgamer.viewmodel.ParticipateViewModel
import com.boardgamer.viewmodel.ProfileViewModel
import com.boardgamer.viewmodel.RegistrationViewModel
import com.boardgamer.viewmodel.SuggestGameViewModel

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
            Home(navController)
        }
        composable(RegistrationViewModel.SCREEN_NAME) {
            Registration(navController)
        }
        composable(CurrentEventsViewModel.SCREEN_NAME + "/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments!!.getString("playerId")!!.toLong()
            CurrentEvents(navController, playerId)
        }
        composable(ProfileViewModel.SCREEN_NAME + "/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments!!.getString("playerId")!!.toLong()
            Profile(navController, playerId)
        }
        composable(GameLibraryViewModel.SCREEN_NAME) {
            GameLibrary(navController)
        }
        composable(NewAppointmentViewModel.SCREEN_NAME + "/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments!!.getString("playerId")!!.toLong()
            NewAppointment(navController, playerId)
        }
        composable(ParticipateViewModel.SCREEN_NAME + "/{playerId}/{id}") { backStackEntry ->
            val playerId = backStackEntry.arguments!!.getString("playerId")!!.toLong()
            val appointmentId = backStackEntry.arguments!!.getString("id")!!.toLong()
            Participate(navController, playerId, appointmentId)
        }
        composable(AppointmentInfosViewModel.SCREEN_NAME + "/{playerId}/{appointmentId}") {
            val playerId = it.arguments!!.getString("playerId")!!.toLong()
            val appointmentId = it.arguments!!.getString("appointmentId")!!.toLong()
            AppointmentInfos(navController, playerId, appointmentId)
        }
        composable(NewMessageViewModel.SCREEN_NAME + "/{playerId}/{appointmentId}") { backStackEntry ->
            val playerId = backStackEntry.arguments!!.getString("playerId")!!.toLong()
            val appointmentId = backStackEntry.arguments!!.getString("appointmentId")!!.toLong()
            NewMessage(navController, playerId, appointmentId)
        }
        composable(SuggestGameViewModel.SCREEN_NAME + "/{appointmentId}") { backStackEntry ->
            val appointmentId = backStackEntry.arguments!!.getString("appointmentId")!!.toLong()
            SuggestGame(navController, appointmentId)
        }
    }
}
