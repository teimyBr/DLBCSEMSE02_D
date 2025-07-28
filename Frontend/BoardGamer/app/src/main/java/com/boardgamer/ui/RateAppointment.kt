package com.boardgamer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boardgamer.R
import com.boardgamer.viewmodel.RateAppointmentViewModel

@Composable
fun RateAppointment(onDismissRequest: () -> Unit, playerId: Long, appointmentId: Long) {
    val viewModel = viewModel<RateAppointmentViewModel>()
    viewModel.setup(onDismissRequest, playerId, appointmentId)

    val ratingHost by viewModel.ratingHost.collectAsState()
    val ratingFood by viewModel.ratingFood.collectAsState()
    val ratingOverall by viewModel.ratingOverall.collectAsState()

    Dialog(
        onDismissRequest = viewModel::dismiss
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.add_evaluation_title),
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Spacer(Modifier.height(16.dp))

                Text(
                    stringResource(R.string.add_evaluation_host_title),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall
                )
                Rating(ratingHost, viewModel::setRatingHost)
                Spacer(Modifier.height(12.dp))

                Text(
                    stringResource(R.string.add_evaluation_food_title),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall
                )
                Rating(ratingFood, viewModel::setRatingFood)
                Spacer(Modifier.height(12.dp))

                Text(
                    stringResource(R.string.add_evaluation_overall_title),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall
                )
                Rating(ratingOverall, viewModel::setRatingOverall)
                Spacer(Modifier.height(12.dp))

                Row {
                    TextButton(onClick = viewModel::dismiss) { Text(stringResource(R.string.cancel)) }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = viewModel::commit) {
                        Text(
                            stringResource(R.string.ok)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Rating(
    rating: Int,
    setRating: (Int) -> Unit
) {
    val outlinedStar = painterResource(R.drawable.star_empty)
    val filledStar = painterResource(R.drawable.star_filled)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(5) { index ->
            Icon(
                painter = if (index < rating) filledStar else outlinedStar,
                contentDescription = "",
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        setRating(index + 1)
                    }
                    .padding(2.dp)
            )
        }
    }
}