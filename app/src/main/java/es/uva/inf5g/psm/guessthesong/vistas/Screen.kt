package es.uva.inf5g.psm.guessthesong.vistas


sealed class Screen(val ruta: String) {


    data object TitleScreen : Screen("title_screen")


    data object Instructions : Screen("instructions")


    data object Register : Screen("register")


    data object EasyLevelTitleScreen :
        Screen("easy_level")


    data object EasySongScreen :
        Screen("easy_song")


    data object MediumLevelTitleScreen :
        Screen("medium_level")


    data object MediumSongScreen :
        Screen("medium_song")

    data object HardLevelTitleScreen : Screen("hard_level")

    data object HardSongScreen : Screen("hard_song")



    data object BonusLevelTitleScreen :
        Screen("bonus_level")



    data object BonusSongScreen :
        Screen("bonus_song")



    data object Ranking :
        Screen("ranking")

}