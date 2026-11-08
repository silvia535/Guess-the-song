package es.uva.inf5g.psm.guessthesong.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import es.uva.inf5g.psm.guessthesong.modelos.player.Player
import es.uva.inf5g.psm.guessthesong.viewmodels.player.PlayerViewModel
import java.util.UUID

@Composable
fun Register(
    onRegisterSuccess: (String) -> Unit
) {

    val playerViewModel: PlayerViewModel = viewModel()

    var playerName by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val message by playerViewModel.message.collectAsState()

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
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "🎤 Bienvenido",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5D1E0)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Introduce tu nombre para comenzar la partida",
                fontSize = 18.sp,
                color = Color(0xFFE9D7FF)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = playerName,
                onValueChange = {
                    playerName = it
                },
                singleLine = true,
                label = {
                    Text(
                        text = "Nombre",
                        color = Color(0xFFE9D7FF)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFF5D1E0),
                    unfocusedBorderColor = Color(0xFFE9D7FF),
                    cursorColor = Color(0xFFF5D1E0),
                    focusedLabelColor = Color(0xFFF5D1E0),
                    unfocusedLabelColor = Color(0xFFE9D7FF)
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {

                    if (playerName.isBlank()) {
                        return@Button
                    }

                    loading = true

                    val playerId =
                        UUID.randomUUID().toString()

                    val player = Player(
                        id = playerId,
                        name = playerName.trim(),
                        score = 0
                    )

                    playerViewModel.addPlayer(player) {

                        loading = false

                        onRegisterSuccess(playerId)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5D1E0),
                    contentColor = Color(0xFF2D1B3D)
                ),
                modifier = Modifier
                    .height(55.dp)
                    .width(180.dp)
            )
            {
                Text(
                    text = "Comenzar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (loading) {
                CircularProgressIndicator(
                    color = Color(0xFFF5D1E0)
                )
            }

            message?.let {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = it,
                    color = Color(0xFFFFD6E8),
                    fontSize = 16.sp
                )
            }
        }
    }
}