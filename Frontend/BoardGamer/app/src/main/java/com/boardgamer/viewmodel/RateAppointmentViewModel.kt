package com.boardgamer.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Evaluation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RateAppointmentViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var callback: () -> Unit
    var playerId = -1L
    var appointmentId = -1L

    val backendApi = BackendAPI()

    private val ratingHostFlow = MutableStateFlow(0)
    val ratingHost = ratingHostFlow.asStateFlow()

    private val ratingFoodFlow = MutableStateFlow(0)
    val ratingFood = ratingFoodFlow.asStateFlow()

    private val ratingOverallFlow = MutableStateFlow(0)
    val ratingOverall = ratingOverallFlow.asStateFlow()

    fun setup(callback: () -> Unit, playerId: Long, appointmentId: Long) {
        this.callback = callback
        this.playerId = playerId
        this.appointmentId = appointmentId
    }

    fun setRatingHost(rating: Int) {
        ratingHostFlow.value = rating
    }

    fun setRatingFood(rating: Int) {
        ratingFoodFlow.value = rating
    }

    fun setRatingOverall(rating: Int) {
        ratingOverallFlow.value = rating
    }

    fun commit() {
        viewModelScope.launch {
            val evaluation = Evaluation(
                playerId = playerId,
                appointmentId = appointmentId,
                hostEvaluation = ratingHost.value,
                mealEvaluation = ratingFood.value,
                overallEvaluation = ratingOverall.value
            )

            val id = backendApi.addEvaluation(evaluation)
            withContext(Dispatchers.Main) {
                if (id > 0) {

                    Toast.makeText(
                        getApplication<Application>().applicationContext,
                        R.string.add_evaluation_success,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        getApplication<Application>().applicationContext,
                        R.string.add_evaluation_failure,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dismiss()
        }
    }

    fun dismiss() = callback()
}