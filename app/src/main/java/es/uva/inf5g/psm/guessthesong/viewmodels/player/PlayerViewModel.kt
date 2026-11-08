package es.uva.inf5g.psm.guessthesong.viewmodels.player

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import es.uva.inf5g.psm.guessthesong.modelos.player.Player
import es.uva.inf5g.psm.guessthesong.modelos.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players = _players.asStateFlow()

    private val _currentPlayer = MutableStateFlow<Player?>(null)
    val currentPlayer = _currentPlayer.asStateFlow()

    private var rankingListener: ListenerRegistration? = null

    // ------------------------------------------------------------
    //  MÉTODOS EXISTENTES (sin cambios)
    // ------------------------------------------------------------

    fun addPlayer(player: Player, onSuccess: () -> Unit) {
        PlayerRepository.addPlayer(player) { ok, msg ->
            if (ok) {
                _currentPlayer.value = player
                onSuccess()
            } else {
                _message.value = msg
            }
        }
    }

    fun loadPlayer(playerId: String) {
        PlayerRepository.getPlayerById(playerId) {
            _currentPlayer.value = it
        }
    }

    /**
     * Añade puntos al jugador.
     * @param onComplete Callback opcional que recibe un Boolean indicando si la operación fue exitosa.
     *                   Se ejecuta **después** de que Firestore confirme la escritura.
     */
    fun addPoints(
        playerId: String,
        points: Int,
        onComplete: ((Boolean) -> Unit)? = null   // <--- NUEVO PARÁMETRO
    ) {
        PlayerRepository.addPoints(playerId, points) { ok, msg ->
            if (!ok) {
                _message.value = msg
            }
            // Notificamos al callback con el resultado
            onComplete?.invoke(ok)
        }
    }

    fun listenRanking() {
        rankingListener?.remove()
        rankingListener = PlayerRepository.listenRanking {
            _players.value = it
        }
    }

    fun updateScore(playerId: String, score: Int) {
        PlayerRepository.updateScore(playerId, score) { ok, msg ->
            if (!ok) {
                _message.value = msg
            }
        }
    }

    fun deletePlayer(playerId: String) {
        PlayerRepository.deletePlayer(playerId) { ok, msg ->
            if (!ok) {
                _message.value = msg
            }
        }
    }

    fun clearCurrentPlayer() {
        _currentPlayer.value = null
    }

    override fun onCleared() {
        rankingListener?.remove()
        super.onCleared()
    }
}