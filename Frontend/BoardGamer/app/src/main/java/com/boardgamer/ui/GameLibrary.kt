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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.model.Game
import com.boardgamer.ui.theme.BackgroundColorGames
import com.boardgamer.viewmodel.GameLibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLibrary(navController: NavController) {
    val viewModel = viewModel<GameLibraryViewModel>()
    val listValues by viewModel.gameFlow.collectAsState()
    val openDialog by viewModel.dialogOpen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.game_lib_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close)
                        )
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
            Text(
                text = stringResource(R.string.game_lib_subtitle),
                modifier = Modifier.padding(10.dp, 5.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(20.dp))
            Row {
                Spacer(Modifier.weight(1f))
                Button(onClick = {
                    viewModel.addGame()
                }) {
                    Text(
                        "+",
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        stringResource(R.string.add),
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColorGames)
            ) {
                items(listValues) { game ->
                    GameItem(game)
                }
            }
            when {
                openDialog -> {
                    AddGameDialog(viewModel)
                }
            }
        }
    }
}

@Composable
fun GameItem(game: Game) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp)
    ) {
        Text(
            text = game.name,
            modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = game.description,
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AddGameDialog(viewModel: GameLibraryViewModel) {
    val name by viewModel.newGameName.collectAsState()
    val description by viewModel.newGameDesc.collectAsState()
    val commitEnabled by viewModel.commitEnabled.collectAsState()
    Dialog(
        onDismissRequest = viewModel::dismissNewGame
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    stringResource(R.string.add_game_title),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = viewModel::nameChange,
                    label = {
                        Text(stringResource(R.string.add_game_name_label))
                    }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = viewModel::descriptionChange,
                    label = {
                        Text(stringResource(R.string.add_game_description_label))
                    }
                )

                Row {
                    TextButton(onClick = viewModel::dismissNewGame) { Text(stringResource(R.string.cancel)) }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = viewModel::commitNewGame, enabled = commitEnabled) {
                        Text(
                            stringResource(R.string.ok)
                        )
                    }
                }
            }
        }
    }
}
