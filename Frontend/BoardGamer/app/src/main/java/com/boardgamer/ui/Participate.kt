package com.boardgamer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.ui.theme.BackgroundColorGames
import com.boardgamer.ui.theme.BackgroundColorGamesThick
import com.boardgamer.viewmodel.GameSuggestionItem
import com.boardgamer.viewmodel.ParticipateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Participate(navController: NavController, playerId: Long, appointmentId: Long) {
    val viewModel: ParticipateViewModel = viewModel()
    viewModel.setAppointment(appointmentId)
    val appointmentDetails by viewModel.appointmentDetails.collectAsState()
    val gameList by viewModel.gameList.collectAsState()

    LaunchedEffect(playerId, appointmentId) {
        viewModel.initialize(playerId, appointmentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.participate_title)) },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp, 10.dp, 5.dp),
                horizontalAlignment = Alignment.End
            ) {
                Button(onClick = {
                    viewModel.participate()
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.Check, contentDescription = "")
                    Text(
                        stringResource(R.string.participate),
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            AppointmentCard(appointmentDetails = appointmentDetails, showButtons = false, navController = navController, playerId = playerId)

            Spacer(Modifier.height(15.dp))

            Text(
                stringResource(R.string.participate_suggest_game_title),
                modifier = Modifier,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                stringResource(R.string.participate_suggest_game_subtitle),
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(5.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColorGames)
            ) {
                items(gameList) { game ->
                    GameSuggestionItem(game)
                }

            }
        }
    }
}

@Composable
fun GameSuggestionItem(item: GameSuggestionItem) {
    val checked by item.selected.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = BackgroundColorGamesThick,
                        radius = this.size.maxDimension
                    )
                },
            text = item.game.name.substring(0, 1),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.width(15.dp))

        Text(
            modifier = Modifier,
            text = item.game.name,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.weight(1f))

        Checkbox(
            checked = checked,
            onCheckedChange = {
                item.updateSelected()
            }
        )
    }
}