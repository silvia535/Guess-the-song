package es.uva.inf5g.psm.guessthesong.vistas

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uva.inf5g.psm.guessthesong.viewmodels.player.PlayerViewModel
import es.uva.inf5g.psm.guessthesong.viewmodels.song.SongViewModel

@Composable
fun EasySongScreen(
    playerId: String,
    onLevelCompleted: () -> Unit
) {

    val songViewModel: SongViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()

    val songs by songViewModel.songs.collectAsState()
    val realtimeSong by songViewModel.currentSong.collectAsState()
    val player by playerViewModel.currentPlayer.collectAsState()
    val loading by songViewModel.loading.collectAsState()
    val message by songViewModel.message.collectAsState()

    var currentIndex by remember { mutableStateOf(0) }
    var answer by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var solvedByMe by remember { mutableStateOf(false) }

    // 🔥 Nuevo estado para evitar clics múltiples
    var isSubmitting by remember { mutableStateOf(false) }

    var remoteSolvedNotified by remember { mutableStateOf(false) }

    LaunchedEffect(playerId) {
        playerViewModel.loadPlayer(playerId)
    }

    LaunchedEffect(Unit) {
        Log.d("EASY_SCREEN", "Cargando canciones easy")
        songViewModel.loadSongsByLevel("easy")
    }

    val currentSong = songs.getOrNull(currentIndex)

    LaunchedEffect(currentSong?.id) {
        currentSong?.let {
            Log.d("EASY_SCREEN", "Observando canción ${it.id}")
            songViewModel.observeSong(it.id)
            answer = ""
            feedback = ""
            solvedByMe = false
            remoteSolvedNotified = false
            isSubmitting = false // Reiniciamos al cambiar de canción
        }
    }

    val isSolved = if (realtimeSong?.id == currentSong?.id) {
        realtimeSong?.data?.solved ?: false
    } else {
        false
    }

    LaunchedEffect(isSolved, currentSong?.id) {
        if (isSolved && !solvedByMe && !remoteSolvedNotified) {
            feedback = "⚠️ Otro jugador ya ha resuelto la canción\nLa respuesta correcta era: ${currentSong?.data?.originalName}"
            answer = ""
            remoteSolvedNotified = true
        }
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
    ) {

        message?.let {
            Text(
                text = "ERROR: $it",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFFFFC8DD)
            )
        } else if (songs.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No hay canciones",
                    color = Color.White,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = message ?: "Firestore devolvió 0 canciones",
                    color = Color.Red
                )
            }
        } else if (currentSong == null) {
            // Nivel completado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎉 Nivel fácil completado",
                    color = Color.White,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Jugador: ${player?.name ?: ""}",
                    color = Color(0xFFF5E9FF)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Puntuación actual: ${player?.score ?: 0}",
                    color = Color(0xFF7CFFB2),
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onLevelCompleted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB48EAD),
                        contentColor = Color.White
                    )
                ) {
                    Text("Continuar al nivel medio")
                }
            }
        } else {
            // Pantalla de juego
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Jugador: ${player?.name ?: ""}",
                    color = Color(0xFFF5E9FF),
                    fontSize = 18.sp
                )

                Text(
                    text = "Puntos: ${player?.score ?: 0}",
                    color = Color(0xFF7CFFB2),
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = currentSong.data.latinName,
                    color = Color.White,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    enabled = !isSolved && !solvedByMe && !isSubmitting,
                    label = { Text("Respuesta") },
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFFFFC8DD),
                        focusedContainerColor = Color(0x22FFFFFF),
                        unfocusedContainerColor = Color(0x11FFFFFF),
                        focusedIndicatorColor = Color(0xFFFFC8DD),
                        unfocusedIndicatorColor = Color(0xFFB48EAD),
                        focusedLabelColor = Color(0xFFFFC8DD),
                        unfocusedLabelColor = Color(0xFFB48EAD)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (feedback.isNotEmpty()) {
                    Text(
                        text = feedback,
                        color = if (solvedByMe) Color(0xFF7CFFB2) else Color(0xFFFF5D8F),
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 🔥 BOTÓN MODIFICADO
                Button(
                    onClick = {
                        // Caso 1: Ya resuelta por mí o por otro → pasar a la siguiente
                        if (solvedByMe || isSolved) {
                            currentIndex++
                            answer = ""
                            feedback = ""
                            solvedByMe = false
                            remoteSolvedNotified = false
                            isSubmitting = false
                            return@Button
                        }

                        // Caso 2: Respuesta vacía o incorrecta
                        if (answer.trim().isEmpty()) {
                            feedback = "✏️ Escribe una respuesta"
                            return@Button
                        }

                        // Caso 3: Comprobar respuesta
                        if (answer.trim().lowercase() == currentSong.data.originalName.trim().lowercase()) {
                            // Evitar múltiples envíos
                            if (isSubmitting) return@Button
                            isSubmitting = true

                            // 1. Intentar resolver la canción
                            songViewModel.solveSong(currentSong.id) { success ->
                                if (success) {
                                    // 2. Sumar punto y esperar a que termine
                                    playerViewModel.addPoints(playerId, 1) { pointsAdded ->
                                        if (pointsAdded) {
                                            // 3. Recargar el jugador (con la nueva puntuación)
                                            playerViewModel.loadPlayer(playerId)
                                            feedback = "💖 Correcto (+1 punto)"
                                            solvedByMe = true
                                        } else {
                                            feedback = "⚠️ Error al sumar el punto"
                                        }
                                        isSubmitting = false
                                    }
                                } else {
                                    feedback = "⚠️ Otro jugador ya lo ha resuelto.\nLa respuesta correcta era: ${currentSong.data.originalName}"
                                    isSubmitting = false
                                }
                            }
                        } else {
                            feedback = "❌ Respuesta incorrecta"
                        }
                    },
                    enabled = !isSubmitting, // 🔥 Deshabilitar mientras se procesa
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB48EAD),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        if (solvedByMe || isSolved)
                            "Continuar"
                        else if (isSubmitting)
                            "Enviando..."
                        else
                            "Comprobar"
                    )
                }
            }
        }
    }
}