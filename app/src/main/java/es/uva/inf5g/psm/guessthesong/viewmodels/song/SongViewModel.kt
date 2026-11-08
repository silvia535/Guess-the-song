package es.uva.inf5g.psm.guessthesong.viewmodels.song

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import es.uva.inf5g.psm.guessthesong.modelos.repository.SongRepository
import es.uva.inf5g.psm.guessthesong.modelos.song.SongDoc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SongViewModel : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _songs = MutableStateFlow<List<SongDoc>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _currentSong = MutableStateFlow<SongDoc?>(null)
    val currentSong = _currentSong.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var listener: ListenerRegistration? = null

    fun loadSongsByLevel(level: String) {

        _loading.value = true
        _songs.value = emptyList()

        SongRepository.getSongsByLevel(
            level = level,
            onResult = { list ->
                _songs.value = list
                _loading.value = false
            },
            onError = { e ->
                _message.value = e.message ?: "Error cargando canciones"
                _loading.value = false
            }
        )
    }

    fun observeSong(songId: String) {

        listener?.remove()

        _currentSong.value = null

        listener = SongRepository.listenSong(songId) { song ->
            _currentSong.value = song
        }
    }

    fun solveSong(
        songId: String,
        onResult: (Boolean) -> Unit
    ) {

        SongRepository.solveSongTransaction(songId) { ok, msg ->

            if (!ok) {
                _message.value = msg
            }

            onResult(ok)
        }
    }

    override fun onCleared() {
        listener?.remove()
        super.onCleared()
    }
}