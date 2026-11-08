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
fun HardLevelTitleScreen(
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
            Color(0xFF120A1A),
            Color(0xFF2A1B3D),
            Color(0xFFB48EAD)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .clickable { onStartLevel() },
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .padding(28.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "NIVEL DIFÍCIL",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5D1E0)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Jugador: ${player?.name ?: ""}",
                color = Color(0xFFF5E9FF),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Reglas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD6E8)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = """
                • Las canciones serán las más difíciles del juego.

                • Debes adivinar el nombre original de la canción.

                • Si aciertas sin usar pistas ganarás 5 puntos.

                • Puedes pedir la pista 1:
                  Se mostrará el álbum de la canción.
                  Si aciertas ganarás 4 puntos.

                • Puedes pedir la pista 2:
                  Aparecerá:
                  "Solicitar diccionario a Julia".
                  Si aciertas después de esta pista ganarás 3 puntos.

                • Las canciones resueltas quedarán bloqueadas.

                • Los puntos se guardarán automáticamente.

                • Al finalizar aparecerá el ranking.

                • IMPORTANTE:
                  Escribe siempre las respuestas en minúscula y sin espacio al final.

                • Pulsa la pantalla para comenzar.
                """.trimIndent(),
                fontSize = 18.sp,
                color = Color(0xFFE9D7FF),
                lineHeight = 28.sp
            )
        }
    }
}