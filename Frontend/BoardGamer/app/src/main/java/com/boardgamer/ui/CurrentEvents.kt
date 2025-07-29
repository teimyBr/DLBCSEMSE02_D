package com.boardgamer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.viewmodel.AppointmentDetails
import com.boardgamer.viewmodel.AppointmentsState
import com.boardgamer.viewmodel.CurrentEventsViewModel
import com.boardgamer.viewmodel.GameLibraryViewModel
import com.boardgamer.viewmodel.NewAppointmentViewModel
import com.boardgamer.viewmodel.ProfileViewModel
import kotlinx.datetime.number

fun kotlinx.datetime.LocalDate.toJavaLocalDate(): java.time.LocalDate =
    java.time.LocalDate.of(year, month.number, day)

fun kotlinx.datetime.LocalDateTime.toJavaLocalDateTime(): java.time.LocalDateTime =
    java.time.LocalDateTime.of(year, month.number, day, hour, minute, second)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentEvents(navController: NavController, playerId: Long) {

    val viewModel: CurrentEventsViewModel = viewModel()
    val appointmentsState by viewModel.appointmentsState.collectAsState()

    LaunchedEffect(playerId) {
        viewModel.initialize(playerId)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshAppointments()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.current_events)
                    )
                },
                actions = {
                    IconButton(onClick = {

                        navController.navigate(ProfileViewModel.SCREEN_NAME + "/$playerId")

                    }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = stringResource(id = R.string.profile)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    if (viewModel.isNextHost) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigate(NewAppointmentViewModel.SCREEN_NAME + "/$playerId")
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.new_event)
                            )
                            Text(
                                text = stringResource(id = R.string.new_event),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {

                                navController.navigate(GameLibraryViewModel.SCREEN_NAME)

                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = stringResource(id = R.string.game_lib_title)
                        )
                        Text(
                            text = stringResource(id = R.string.game_lib_title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = appointmentsState) {
                is AppointmentsState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is AppointmentsState.Success -> {
                    AppointmentList(state.appointments, navController, playerId)
                }

                is AppointmentsState.Error -> {
                    Text(
                        text = stringResource(id = state.messageResId),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentList(
    appointments: List<AppointmentDetails>,
    navController: NavController,
    playerId: Long
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(appointments) { appointmentDetails ->
            AppointmentCard(
                appointmentDetails = appointmentDetails,
                showButtons = true,
                navController = navController,
                playerId = playerId
            )
        }
    }
}