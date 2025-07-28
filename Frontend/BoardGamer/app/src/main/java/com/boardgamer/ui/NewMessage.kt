package com.boardgamer.ui

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.viewmodel.NewMessageViewModel
import com.boardgamer.viewmodel.SaveState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMessage(navController: NavController, playerId: Long, appointmentId: Long) {
    val viewModel: NewMessageViewModel = viewModel()
    val messageText by viewModel.messageText.collectAsState()

    val message15min = stringResource(id = R.string.message_quick_15)
    val message30min = stringResource(id = R.string.message_quick_30)
    val message45min = stringResource(id = R.string.message_quick_45)
    val message60min = stringResource(id = R.string.message_quick_60)

    LaunchedEffect(key1 = Unit) {
        viewModel.initialize(playerId, appointmentId)
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
                title = {
                    Text(
                        text = stringResource(id = R.string.message_headline)
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.message_quick),
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = {
                    viewModel.postMessage(message15min)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) { Text(
                text = message15min
            ) }

            OutlinedButton(
                onClick = {
                    viewModel.postMessage(message30min)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text(
                    text = message30min
            ) }

            OutlinedButton(
                onClick = {
                    viewModel.postMessage(message45min)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text(
                    text = message45min
            ) }

            OutlinedButton(
                onClick = {
                    viewModel.postMessage(message60min)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text(
                    text = message60min
            ) }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = messageText,
                onValueChange = viewModel::onMessageChange,
                label = {
                    Text(
                        text = stringResource(id = R.string.message_own)
                    ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.postMessage(messageText)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text(
                    text = stringResource(id = R.string.message_post)
                )
            }
        }
    }
}