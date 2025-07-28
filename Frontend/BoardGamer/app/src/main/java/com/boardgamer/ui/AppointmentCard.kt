package com.boardgamer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.viewmodel.AppointmentDetails
import com.boardgamer.viewmodel.AppointmentInfosViewModel
import com.boardgamer.viewmodel.ParticipateViewModel
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun AppointmentCard(
    appointmentDetails: AppointmentDetails,
    showButtons: Boolean,
    navController: NavController,
    playerId: Long
) {
    val appointment = appointmentDetails.appointment
    val today = java.time.LocalDate.now()
    val isPast = appointment.date.toJavaLocalDate() < today
    val titleResourceId = if (isPast) R.string.past_event_hostname else R.string.next_event_hostname

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = stringResource(
                    id = titleResourceId,
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

            if (showButtons) {
                if (isPast) {
                    PastAppointmentButtons(
                        appointmentDetails = appointmentDetails,
                        playerId = playerId
                    )
                } else {
                    FutureAppointmentButtons(
                        appointmentId = appointmentDetails.appointment.id,
                        canParticipate = appointmentDetails.canParticipate,
                        navController = navController,
                        playerId = playerId
                    )
                }
            }
        }
    }
}

@Composable
private fun PastAppointmentButtons(
    appointmentDetails: AppointmentDetails,
    playerId: Long
) {
    val openDialog by appointmentDetails.openDialog.collectAsState()

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
            onDismissRequest = appointmentDetails::updateOpenDialog,
            playerId = playerId,
            appointmentId = appointmentDetails.appointment.id
        )
    }
}

@Composable
private fun FutureAppointmentButtons(
    appointmentId: Long,
    canParticipate: Boolean,
    navController: NavController,
    playerId: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                navController.navigate("${AppointmentInfosViewModel.SCREEN_NAME}/$playerId/$appointmentId")
            },
            modifier = Modifier.weight(1f),
            shape = RectangleShape
        ) {
            Text(
                text = stringResource(id = R.string.event_show_informations)
            )
        }
        if (canParticipate) {
            Button(
                onClick = {
                    navController.navigate("${ParticipateViewModel.SCREEN_NAME}/$playerId/$appointmentId")
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