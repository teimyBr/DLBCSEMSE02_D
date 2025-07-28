package com.boardgamer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.ui.theme.DarkGreen
import com.boardgamer.ui.theme.DarkRed
import com.boardgamer.viewmodel.AppointmentInfosViewModel
import com.boardgamer.viewmodel.GameVoteItem
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentInfos(navController: NavController, playerId: Long, appointmentId: Long) {
    val viewModel = viewModel<AppointmentInfosViewModel>()

    val participants by viewModel.participants.collectAsState()
    val games by viewModel.gameSuggestion.collectAsState()
    val message by viewModel.messages.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    LaunchedEffect(key1 = playerId, key2 = appointmentId) {
        viewModel.setState(playerId, appointmentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            stringResource(R.string.infos_title),
                            Modifier,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            subtitle,
                            Modifier,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.infos_participants),
                Modifier,
                style = MaterialTheme.typography.titleLarge
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                LazyRow {
                    items(participants) { participant ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "")
                            Text(
                                participant.name,
                                Modifier,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                    }
                }
            }

            Spacer(Modifier.height(5.dp))

            Text(
                stringResource(R.string.infos_suggested_games),
                Modifier,
                style = MaterialTheme.typography.titleLarge
            )

            Card(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        Modifier.weight(1f)
                    ) {
                        items(games) { game ->
                            GameVoteItem(game, viewModel)
                        }
                    }
                    Button(
                        onClick = {
                            //Navigate to the add game view
                        }
                    ) {
                        Text(
                            stringResource(R.string.infos_suggest_game),
                            Modifier,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(5.dp))

            Text(
                stringResource(R.string.infos_messages),
                Modifier,
                style = MaterialTheme.typography.titleLarge
            )

            Card(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        Modifier.weight(1f)
                    ) {
                        items(message) { message ->
                            Column {
                                Text(
                                    message.timestamp.toJavaLocalDateTime()
                                        .format(dateFormatter)
                                            + " - "
                                            + message.timestamp.toJavaLocalDateTime()
                                        .format(timeFormatter),
                                    Modifier,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    message.message,
                                    Modifier,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.height(1.dp))
                            }
                        }
                    }

                    Button(
                        onClick = {
                            //Navigate to the add message screen
                        }
                    ) {
                        Text(
                            stringResource(R.string.infos_write_message),
                            Modifier,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameVoteItem(gameVoteItem: GameVoteItem, viewModel: AppointmentInfosViewModel) {
    val colorAgainst =
        if (gameVoteItem.gameVote.value) Color.LightGray else DarkRed
    val colorFor =
        if (gameVoteItem.gameVote.value) DarkGreen else Color.LightGray
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(gameVoteItem.game.name, Modifier, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, Color.DarkGray),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray),
            contentPadding = PaddingValues(0.dp, 0.dp),
            modifier = Modifier
                .padding(0.dp)
        ) {
            Button(
                onClick = { viewModel.updateGameVote(gameVoteItem, false) },
                colors = ButtonDefaults.buttonColors(colorAgainst),
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
                contentPadding = PaddingValues(5.dp),
                modifier = Modifier.padding(5.dp, 0.dp)
            ) {
                Text(
                    stringResource(R.string.infos_against),
                    Modifier,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            VerticalDivider(Modifier.width(2.dp), color = Color.DarkGray)

            Button(
                onClick = { viewModel.updateGameVote(gameVoteItem, true) },
                colors = ButtonDefaults.buttonColors(colorFor),
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
                contentPadding = PaddingValues(5.dp),
                modifier = Modifier.padding(5.dp, 0.dp)
            ) {
                Text(
                    stringResource(R.string.infos_for),
                    Modifier,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}