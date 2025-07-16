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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boardgamer.model.Game
import com.boardgamer.viewmodel.GameLibraryViewModel

//TODO move strings into strings.xml
@Composable
fun GameLibrary() {
    val viewModel = viewModel<GameLibraryViewModel>()
    val listValues = viewModel.gameFlow.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp, 30.dp, 5.dp, 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Spielebiblothek",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                // Navigate to appointment view, not yet implemented so empty for now
            }) { Text("X") }
        }
        Text(
            text = "Spiele die für zukünftige Events vorgeschlagen werden können",
            modifier = Modifier.padding(10.dp, 5.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(20.dp))
        Row {
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                //Show a pop up that allows people Enter title + description of new game
            }) {
                Text(
                    "+",
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Hinzufügen",
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(159, 168, 218, 70))
        ) {
            items(listValues.value) { game ->
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
