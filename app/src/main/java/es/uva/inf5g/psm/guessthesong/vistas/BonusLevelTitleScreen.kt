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
fun BonusLevelTitleScreen(
    playerId: String,
    onStartLevel: () -> Unit
){

    val playerViewModel: PlayerViewModel = viewModel()
    val player by playerViewModel.currentPlayer.collectAsState()

    LaunchedEffect(playerId){
        playerViewModel.loadPlayer(playerId)
    }

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
            .clickable {
                onStartLevel()
            },
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(
                text = "⭐ NIVEL BONUS ⭐",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD166)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Jugador: ${player?.name ?: ""}",
                color = Color(0xFFF5E9FF),
                fontSize = 18.sp
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
                text = """
                • Nivel especial bonus.
                • Solo tendrás una oportunidad.
                • No existen pistas.
                • Si aciertas la canción ganarás 10 puntos.
                • La canción se bloqueará para todos.
                • Al finalizar aparecerá el ranking.
                """.trimIndent(),
                fontSize = 18.sp,
                color = Color(0xFFE9D7FF),
                lineHeight = 28.sp
            )
        }
    }
}