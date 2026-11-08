package es.uva.inf5g.psm.guessthesong.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun EasyLevelTitleScreen(
    playerId: String,
    onStartLevel: () -> Unit
) {

    val playerViewModel: PlayerViewModel = viewModel()

    val player by playerViewModel.currentPlayer.collectAsState()

    LaunchedEffect(playerId) {
        playerViewModel.loadPlayer(playerId)
    }

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
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "NIVEL FÁCIL",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5D1E0)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Jugador: ${player?.name ?: ""}",
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Reglas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD6E8)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text =
                    """
                • Se mostrará una canción en latín.
                
                • Debes adivinar su nombre original.
                
                • El primer jugador que acierte obtiene un punto.
                
                • Una vez resuelta quedará bloqueada para todos.
                
                • Si ya está resuelta podrás pasar a la siguiente.
                
                • Al finalizar se mostrará el ranking.
                
                • IMPORTANTE:
                  Escribe todas las respuestas en minúscula 
                  aunque sean nombres propios y sin espacios al final.
                """.trimIndent(),
                fontSize = 18.sp,
                color = Color(0xFFE9D7FF),
                lineHeight = 28.sp
            )
        }
    }
}