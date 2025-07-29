package com.boardgamer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.ui.theme.BackgroundColorGames
import com.boardgamer.ui.theme.GreenButton
import com.boardgamer.viewmodel.SaveState
import com.boardgamer.viewmodel.SuggestGameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestGame(navController: NavController, appointmentId: Long) {
    val viewModel = viewModel<SuggestGameViewModel>()
    viewModel.initialize(appointmentId)
    val games by viewModel.gameList.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.saveState.collect {
            if (it is SaveState.Success) {
                navController.popBackStack()
                viewModel.resetSaveState()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.participate_suggest_game_title))
                },
                navigationIcon = {
                    IconButton({ navController.popBackStack() }) {
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
                Button(
                    onClick = { viewModel.suggest() },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenButton
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = "")
                    Text(
                        stringResource(R.string.suggest_game_submit),
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
                .padding(16.dp)
        ) {
            Text(
                text = subtitle,
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.suggest_game_note),
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColorGames)
            ) {
                items(games) { game ->
                    GameSuggestionItem(game)
                }
            }
        }
    }

}