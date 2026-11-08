package es.uva.inf5g.psm.guessthesong.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uva.inf5g.psm.guessthesong.viewmodels.player.PlayerViewModel


@Composable
fun MediumLevelTitleScreen(
    playerId: String,
    onStartLevel: () -> Unit
) {

    val playerViewModel: PlayerViewModel = viewModel()

    val player by playerViewModel.currentPlayer.collectAsState()


    LaunchedEffect(playerId) {

        playerViewModel.loadPlayer(playerId)

    }


    val scrollState = rememberScrollState()


    val background = Brush.verticalGradient(

        colors = listOf(

            Color(0xFF1A1B2E),
            Color(0xFF3B2F4A),
            Color(0xFFB48EAD)

        )

    )



    Box(

        modifier = Modifier

            .fillMaxSize()

            .background(background)

            .clickable {

                onStartLevel()

            },


        contentAlignment = Alignment.Center

    ) {



        Column(

            modifier = Modifier

                .padding(28.dp)

                .verticalScroll(scrollState),


            horizontalAlignment = Alignment.CenterHorizontally

        ) {



            Text(

                text = "NIVEL MEDIO",

                fontSize = 36.sp,

                fontWeight = FontWeight.Bold,

                color = Color(0xFFF5D1E0)

            )



            Spacer(
                modifier = Modifier.height(24.dp)
            )



            Text(

                text = "Jugador: ${player?.name ?: ""}",

                fontSize = 18.sp,

                color = Color.White

            )



            Spacer(
                modifier = Modifier.height(24.dp)
            )



            Text(

                text = "Reglas",

                fontSize = 28.sp,

                fontWeight = FontWeight.Bold,

                color = Color(0xFFFFD6E8)

            )



            Spacer(
                modifier = Modifier.height(20.dp)
            )



            Text(

                text =
                    """
                    
                • Se mostrará el nombre de una canción escrito en latín.
                
                • Debes adivinar el nombre original de la canción.
                
                • Las canciones serán más difíciles que en el nivel fácil.
                
                • Si aciertas sin pedir pistas ganarás 3 puntos.
                
                • Puedes solicitar la pista 1:
                  Se mostrará el álbum al que pertenece la canción.
                  Si aciertas después de esta pista ganarás 2 puntos.
                
                • Puedes solicitar la pista 2:
                  Aparecerá el mensaje:
                  "Solicitar diccionario a Julia".
                  
                  Si aciertas después de esta pista ganarás 1 punto.
                
                • Las pistas no podrán cambiarse una vez usadas.
                
                • Cuando un jugador resuelva una canción quedará bloqueada
                  para todos los jugadores.
                
                • Si otro jugador ya la ha resuelto podrás continuar
                  directamente con la siguiente canción.
                
                • Los puntos obtenidos se guardarán automáticamente.
                
                • Al finalizar el nivel se mostrará el ranking actualizado.
                
                • IMPORTANTE:
                  Escribe todas las respuestas en minúscula 
                  aunque sean nombres propios y sin espacios al final.
                  
                • Pulsa en cualquier lugar de la pantalla para comenzar.
                
                """.trimIndent(),


                fontSize = 18.sp,


                color = Color(0xFFE9D7FF),


                lineHeight = 28.sp

            )

        }

    }

}