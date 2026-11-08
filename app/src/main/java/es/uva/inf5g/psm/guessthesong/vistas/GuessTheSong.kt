package es.uva.inf5g.psm.guessthesong.vistas


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Composable
fun GuessTheSong(
    paddingValues: PaddingValues
) {


    val navController = rememberNavController()



    NavHost(

        navController = navController,

        startDestination = Screen.TitleScreen.ruta

    ) {



        /*
         * Pantalla inicial
         */
        composable(
            route = Screen.TitleScreen.ruta
        ) {


            TitleScreen {


                navController.navigate(
                    Screen.Instructions.ruta
                )

            }

        }





        /*
         * Instrucciones
         */
        composable(
            route = Screen.Instructions.ruta
        ) {


            Instructions {


                navController.navigate(
                    Screen.Register.ruta
                )

            }

        }





        /*
         * Registro jugador
         */
        composable(
            route = Screen.Register.ruta
        ) {


            Register { playerId ->


                navController.navigate(

                    "${Screen.EasyLevelTitleScreen.ruta}/$playerId"

                )


            }


        }






        /*
         * Nivel fácil - reglas
         */
        composable(

            route =
                "${Screen.EasyLevelTitleScreen.ruta}/{playerId}"

        ){ backStackEntry ->


            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            EasyLevelTitleScreen(

                playerId = playerId,


                onStartLevel = {


                    navController.navigate(

                        "${Screen.EasySongScreen.ruta}/$playerId"

                    )


                }

            )


        }








        /*
         * Nivel fácil - canciones
         */
        composable(

            route =
                "${Screen.EasySongScreen.ruta}/{playerId}"

        ){ backStackEntry ->



            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            EasySongScreen(

                playerId = playerId,


                onLevelCompleted = {


                    navController.navigate(

                        "${Screen.MediumLevelTitleScreen.ruta}/$playerId"

                    )


                }


            )


        }








        /*
         * Nivel medio - reglas
         */
        composable(

            route =
                "${Screen.MediumLevelTitleScreen.ruta}/{playerId}"

        ){ backStackEntry ->


            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            MediumLevelTitleScreen(

                playerId = playerId,


                onStartLevel = {


                    navController.navigate(

                        "${Screen.MediumSongScreen.ruta}/$playerId"

                    )


                }

            )

        }








        /*
         * Nivel medio - canciones
         */
        composable(

            route =
                "${Screen.MediumSongScreen.ruta}/{playerId}"

        ){ backStackEntry ->


            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            MediumSongScreen(

                playerId = playerId,


                onLevelCompleted = {


                    navController.navigate(

                        "${Screen.HardLevelTitleScreen.ruta}/$playerId"

                    )


                }


            )


        }









        /*
         * Nivel difícil - reglas
         */
        composable(

            route =
                "${Screen.HardLevelTitleScreen.ruta}/{playerId}"

        ){ backStackEntry ->



            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            HardLevelTitleScreen(

                playerId = playerId,


                onStartLevel = {


                    navController.navigate(

                        "${Screen.HardSongScreen.ruta}/$playerId"

                    )


                }


            )


        }









        /*
         * Nivel difícil - canciones
         */
        composable(

            route =
                "${Screen.HardSongScreen.ruta}/{playerId}"

        ){ backStackEntry ->



            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            HardSongScreen(

                playerId = playerId,


                onLevelCompleted = {


                    navController.navigate(

                        "${Screen.BonusLevelTitleScreen.ruta}/$playerId"

                    )


                }


            )


        }









        /*
         * BONUS - reglas
         */
        composable(

            route =
                "${Screen.BonusLevelTitleScreen.ruta}/{playerId}"

        ){ backStackEntry ->



            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            BonusLevelTitleScreen(

                playerId = playerId,


                onStartLevel = {


                    navController.navigate(

                        "${Screen.BonusSongScreen.ruta}/$playerId"

                    )


                }


            )


        }









        /*
         * BONUS - canción
         */
        composable(

            route =
                "${Screen.BonusSongScreen.ruta}/{playerId}"

        ){ backStackEntry ->



            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            BonusSongScreen(

                playerId = playerId,


                onLevelCompleted = {


                    navController.navigate(

                        "${Screen.Ranking.ruta}/$playerId"

                    )


                }


            )


        }









        /*
         * RANKING FINAL
         */
        composable(

            route =
                "${Screen.Ranking.ruta}/{playerId}"

        ){ backStackEntry ->



            val playerId =

                backStackEntry.arguments
                    ?.getString("playerId")
                    ?: ""



            RankingScreen(

                //playerId = playerId

            )


        }



    }

}