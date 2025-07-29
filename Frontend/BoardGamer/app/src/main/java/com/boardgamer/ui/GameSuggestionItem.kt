package com.boardgamer.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import com.boardgamer.ui.theme.BackgroundColorGamesThick
import com.boardgamer.viewmodel.GameSuggestionItem

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