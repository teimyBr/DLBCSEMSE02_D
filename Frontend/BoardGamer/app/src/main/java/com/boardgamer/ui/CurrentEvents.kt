package com.boardgamer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.model.SessionManager
import com.boardgamer.viewmodel.AppointmentDetails
import com.boardgamer.viewmodel.AppointmentsState
import com.boardgamer.viewmodel.CurrentEventsViewModel
import com.boardgamer.viewmodel.GameLibraryViewModel
import com.boardgamer.viewmodel.NewAppointmentViewModel
import kotlinx.datetime.number
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

fun kotlinx.datetime.LocalDate.toJavaLocalDate(): java.time.LocalDate =
    java.time.LocalDate.of(year, month.number, day)

fun kotlinx.datetime.LocalDateTime.toJavaLocalDateTime(): java.time.LocalDateTime =
    java.time.LocalDateTime.of(year, month.number, day, hour, minute, second)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentEvents(navController: NavController) {
    val viewModel: CurrentEventsViewModel = viewModel()
    val appointmentsState by viewModel.appointmentsState.collectAsState()

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

                        // Hier kommt der Code zum Aufrufen des eigenen Profils rein

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
                                    navController.navigate(NewAppointmentViewModel.SCREEN_NAME)
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.new_event)
                            )
                            Text(
                                text = stringResource(id = R.string.new_event),
                                fontSize = 12.sp
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
                            fontSize = 12.sp
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
                    AppointmentList(appointments = state.appointments)
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
fun AppointmentList(appointments: List<AppointmentDetails>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(appointments) { appointmentDetails ->
            AppointmentCard(appointmentDetails = appointmentDetails)
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun AppointmentCard(appointmentDetails: AppointmentDetails) {
    val appointment = appointmentDetails.appointment
    val today = java.time.LocalDate.now()
    val isPast = appointment.date.toJavaLocalDate() < today
    val openDialog by appointmentDetails.openDialog.collectAsState()

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = stringResource(
                    id = R.string.next_event_hostname,
                    appointmentDetails.hostName
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(
                    id = R.string.event_date,
                    appointment.date.toJavaLocalDate().format(dateFormatter)
                )
            )
            Text(
                text = stringResource(
                    id = R.string.event_time,
                    appointment.timestamp.toJavaLocalDateTime().format(timeFormatter)
                )
            )
            Text(
                text = stringResource(id = R.string.event_location, appointment.location)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isPast) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier.padding(vertical = 4.dp)

                    ) {
                        Text(
                            text = stringResource(id = R.string.event_isPast),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            appointmentDetails.updateOpenDialog()
                        },
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )

                    ) {
                        Text(
                            text = stringResource(id = R.string.event_set_evaluation)
                        )
                    }
                }
                when {
                    openDialog -> RateAppointment(
                        appointmentDetails::updateOpenDialog,
                        SessionManager.currentPlayer.id,
                        appointmentDetails.appointment.id
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {

                            //Code zum Anzeigen weiterer Informationen kommen hier hin

                        },
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape
                    ) {
                        Text(
                            text = stringResource(id = R.string.event_show_informations)
                        )
                    }
                    Button(
                        onClick = {

                            //Code, um an einem Event teilzunehmen

                        },
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape
                    ) {
                        Text(
                            text = stringResource(id = R.string.event_participate)
                        )
                    }
                }
            }
        }
    }
}