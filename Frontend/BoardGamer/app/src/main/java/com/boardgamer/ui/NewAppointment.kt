package com.boardgamer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.viewmodel.NewAppointmentViewModel
import com.boardgamer.viewmodel.SaveState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAppointment(navController: NavController, playerId: Long) {
    val viewModel: NewAppointmentViewModel = viewModel()

    val date by viewModel.date.collectAsState()
    val time by viewModel.time.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isLocationDifferent by viewModel.isLocationDifferent.collectAsState()
    val customLocation by viewModel.customLocation.collectAsState()

    LaunchedEffect(key1 = playerId) {
        viewModel.initialize(playerId)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.saveState.collect { state ->
            if (state is SaveState.Success) {
                navController.popBackStack()
                viewModel.resetSaveState()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.new_appointment_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(id = R.string.close))
                    }
                }
            )
        }
    ) { innerPadding ->
        val saveState by viewModel.saveState.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = { viewModel.onDateChange(it) },
                label = { Text(
                    text = stringResource(id = R.string.date_label))
                },
                placeholder = { Text(
                    text = stringResource(id = R.string.placeholder_date)
                )},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = time,
                onValueChange = { viewModel.onTimeChange(it) },
                label = { Text(
                    text = stringResource(id = R.string.time_label)
                ) },
                placeholder = { Text(
                    text = stringResource(id = R.string.placeholder_time)
                )},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { viewModel.onNotesChange(it) },
                label = { Text(
                    text = stringResource(id = R.string.notes_label)
                ) },
                placeholder = { Text(
                    text = stringResource(id = R.string.notes_placeholder)
                ) },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = isLocationDifferent,
                    onCheckedChange = { viewModel.onLocationSwitchChange(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.location_switch_label)
                )
            }

            AnimatedVisibility(visible = isLocationDifferent) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = customLocation,
                        onValueChange = { viewModel.onCustomLocationChange(it) },
                        label = { Text(stringResource(id = R.string.custom_location_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (saveState is SaveState.Error) {
                Text(
                    text = stringResource(id = (saveState as SaveState.Error).messageResId),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.saveAppointment() },
                modifier = Modifier.fillMaxWidth(),
                enabled = saveState !is SaveState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                if (saveState is SaveState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(stringResource(id = R.string.create_appointment_button))
                }
            }
        }
    }
}