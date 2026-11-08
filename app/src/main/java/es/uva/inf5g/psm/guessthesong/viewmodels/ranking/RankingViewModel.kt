package es.uva.inf5g.psm.guessthesong.viewmodels.ranking


import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import es.uva.inf5g.psm.guessthesong.modelos.player.Player
import es.uva.inf5g.psm.guessthesong.modelos.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow



class RankingViewModel:ViewModel(){



    private val _ranking =

        MutableStateFlow<List<Player>>(
            emptyList()
        )



    val ranking =

        _ranking.asStateFlow()



    private var listener:
            ListenerRegistration? = null




    fun observeRanking(){



        listener?.remove()



        listener =

            PlayerRepository.listenRanking {

                _ranking.value = it

            }


    }





    override fun onCleared(){


        listener?.remove()


        super.onCleared()

    }


}