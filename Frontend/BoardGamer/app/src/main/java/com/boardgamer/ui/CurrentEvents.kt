package com.boardgamer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.model.Appointment
import com.boardgamer.viewmodel.AppointmentsState
import com.boardgamer.viewmodel.CurrentEventsViewModel
import kotlinx.datetime.number
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

/*
Noch zu erledigen:
- HostID durch Namen tauschen
- Strings anpassen
- Funktion Profil aufrufen implementieren
- Funktion Event bewerten implementieren
- Funktionen zum Anzeigen von Spielevent-Infos und zum Teilnehmen implementieren
- BottomBar optisch anpassen und die Funktion GameLibrary ergänzen und Neues Treffen implementieren
- Colors anpassen
 */

fun kotlinx.datetime.LocalDate.toJavaLocalDate(): java.time.LocalDate =
    java.time.LocalDate.of(year, month.number, day)

fun kotlinx.datetime.LocalDateTime.toJavaLocalDateTime(): java.time.LocalDateTime =
    java.time.LocalDateTime.of(year, month.number, day, hour, minute, second)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentEvents(navController: NavController) {
    val viewModel: CurrentEventsViewModel = viewModel()
    val appointmentsState by viewModel.appointmentsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aktuelles") },
                actions = {
                    IconButton(onClick = { /* Hier kommt der Code zum Aufrufen des eigenen Profils rein */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Profil")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(60.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { /* Code zum ERstellen eines Treffens */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Neues Treffen")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Neues Treffen")
                    }
                    TextButton(onClick = { /* Code zum Anzeigen der GameLibrary */ }) {
                        Text("Spielebibliothek")
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
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
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
fun AppointmentList(appointments: List<Appointment>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(appointments) { appointment ->
            AppointmentCard(appointment = appointment)
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun AppointmentCard(appointment: Appointment) {
    val today = java.time.LocalDate.now()
    val isPast = appointment.date.toJavaLocalDate() < today

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPast){
                MaterialTheme.colorScheme.surfaceContainer
            } else{
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Hier muss ich noch die HostID durch den Hostnamen austauschen - erstmal nur PLatzhalter
            Text(
                text = "Nächstes Treffen bei Host ${appointment.hostId}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Datum: ${appointment.date.toJavaLocalDate().format(dateFormatter)}")
            Text("Uhrzeit: ${appointment.timestamp.toJavaLocalDateTime().format(timeFormatter)} Uhr")
            Text("Ort: ${appointment.location}")
            Spacer(modifier = Modifier.height(16.dp))

            if (isPast) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "Dieses Event liegt in der Vergangenheit",
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* Hier noch der Code um das Event anschließend bewerten zz können */ },
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text("Bewertung abgeben")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { /* Code zum Anzeigen weiterer Informationen kommen hier hin */ },
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape) {
                        Text("Infos")
                    }
                    Button(onClick = { /* Code, um an einem Event teilzunehmen */ },
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape) {
                        Text("Teilnehmen")
                    }
                }
            }
        }
    }
}