package com.boardgamer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boardgamer.model.Game
import com.boardgamer.viewmodel.GameLibraryViewModel

@Composable
fun GameLibrary() {
    val viewModel = viewModel<GameLibraryViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Spielebiblothek")
            Button(onClick = {
                // Navigate to appointment view, not yet implemented so empty for now
            }) { Text("X") }
        }
        Text("Spiele die für zukünftige Events vorgeschlagen werden können")
        Button(onClick = {
            //Show a pop up that allows people Enter title + description of new game
        }) { }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.getGames()) { game ->
                GameItem(game, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun GameItem(game: Game, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = game.name)
        Text(text = game.description)
    }
}
