package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Game
import kotlinx.coroutines.runBlocking

class GameLibraryViewModel : ViewModel() {
    val backend = BackendAPI()

    //TODO figure out threading here, this should likely be handled by flows,
    // so that the backend communication runs on a background thread,
    // and we go back into the ui thread when we have the list of games & need to render it
    fun getGames(): List<Game> {
        return runBlocking { backend.getGames() }
    }
}