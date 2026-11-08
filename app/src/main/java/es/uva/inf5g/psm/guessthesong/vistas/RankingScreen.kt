package es.uva.inf5g.psm.guessthesong.vistas


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uva.inf5g.psm.guessthesong.viewmodels.ranking.RankingViewModel



@Composable
fun RankingScreen(){



    val viewModel:
            RankingViewModel = viewModel()



    val ranking by
    viewModel.ranking.collectAsState()



    LaunchedEffect(Unit){

        viewModel.observeRanking()

    }



    val background =
        Brush.verticalGradient(
            listOf(
                Color(0xFF120A1A),
                Color(0xFF2A1B3D)
            )
        )



    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .background(background)
                .padding(24.dp)

    ){



        Column(

            modifier =
                Modifier.fillMaxSize(),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ){



            Text(

                text="🏆 Ranking",

                color=Color.White,

                fontSize=32.sp

            )



            Spacer(
                Modifier.height(30.dp)
            )




            ranking.forEachIndexed {
                    index,player ->



                Card(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(6.dp)

                ){


                    Row(

                        modifier =
                            Modifier
                                .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ){



                        Text(

                            "${index+1}. ${player.name}",

                            fontSize=20.sp

                        )



                        Spacer(
                            Modifier.weight(1f)
                        )



                        Text(

                            "${player.score} pts",

                            color =
                                Color.Black,

                            fontSize=18.sp

                        )


                    }


                }



            }



        }



    }



}