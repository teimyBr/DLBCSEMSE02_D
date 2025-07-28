package com.boardgamer.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.model.Player
import com.boardgamer.viewmodel.HomeViewModel
import com.boardgamer.viewmodel.LastEventDetails
import com.boardgamer.viewmodel.ProfileViewModel
import com.boardgamer.viewmodel.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController, playerId: Long) {
    val viewModel: ProfileViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize(playerId)
    }

    LaunchedEffect(uiState.loggedOut) {
        if (uiState.loggedOut) {
            navController.navigate(HomeViewModel.SCREEN_NAME) {
                popUpTo(0)
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(R.string.profile_welcome_message, uiState.userName)
                )},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerGroupSection(uiState)

            Spacer(modifier = Modifier.height(24.dp))

            LastEventSection(uiState.lastEvent)

            Spacer(modifier = Modifier.weight(1f))

            ActionButtons(viewModel)
        }
    }
}

@Composable
fun PlayerGroupSection(uiState: ProfileUiState) {
    val nextHost = uiState.nextHost
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start)
    {
        Text(
            text = stringResource(id = R.string.playergroup)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.padding(16.dp))
            {
                if (uiState.playerGroup.isEmpty()) {
                    CircularProgressIndicator()
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(uiState.playerGroup) { player ->
                            PlayerIcon(player)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val textToShow = if (nextHost != null) {
                    stringResource(R.string.next_host_announcement, nextHost.name)
                } else {
                    stringResource(R.string.next_host_pending)
                }
                Text(text = textToShow)
            }
        }
    }
}

@Composable
fun LastEventSection(lastEvent: LastEventDetails?) {
    if (lastEvent != null) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text(
                text = stringResource(id = R.string.last_event)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.last_event_date, lastEvent.date)
                    )
                    RatingBar(R.string.rating_label_host, lastEvent.hostRating)
                    RatingBar(R.string.rating_label_general, lastEvent.generalRating)
                }
            }
        }
    }
}

@Composable
fun RatingBar(@StringRes labelResId: Int, percentage: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = labelResId)
        )
        Text(
            text = stringResource(id = R.string.rating_percentage, percentage),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PlayerIcon(player: Player) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = player.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp)
        )
        Text(player.name, fontSize = 12.sp)
    }
}


@Composable
fun ActionButtons(viewModel: ProfileViewModel) {
    val showAboutDialog = remember { mutableStateOf(false) }

    if (showAboutDialog.value) {
        AlertDialog(
            onDismissRequest = { showAboutDialog.value = false },
            title = { Text(
                text = stringResource(id = R.string.about_boardgamer)
            ) },
            text = { Text(
                text = stringResource(id = R.string.about_boardgamer_text)
            ) },
            confirmButton = {
                Button(onClick = {
                    showAboutDialog.value = false
                },
                    shape = RectangleShape)
                {
                    Text(
                        text = stringResource(id = R.string.close)
                    )
                }
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Button(
            onClick = { showAboutDialog.value = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape
        ) {
            Text(
                text = stringResource(id = R.string.about_boardgamer)
            )
        }
        Button(
            onClick = { viewModel.logout() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape
        ) {
            Text(
                text = stringResource(id = R.string.logout)
            )
        }
    }
}
