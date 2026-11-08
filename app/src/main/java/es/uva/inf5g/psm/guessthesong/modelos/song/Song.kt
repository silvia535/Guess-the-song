package es.uva.inf5g.psm.guessthesong.modelos.song

data class Song(
    //val id: String = "",
    val latinName: String = "",
    val originalName: String = "",
    val solved: Boolean = false,
    val level: String = "", // easy, medium, hard
    val album: String= "",
    val order: Int = 0
)