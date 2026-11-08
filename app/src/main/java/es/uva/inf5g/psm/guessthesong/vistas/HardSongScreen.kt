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
fun HardSongScreen(
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

    var index by remember { mutableStateOf(0) }

    var answer by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    var solved by remember { mutableStateOf(false) }

    var usedHint1 by remember { mutableStateOf(false) }
    var usedHint2 by remember { mutableStateOf(false) }

    var showAlbum by remember { mutableStateOf(false) }

    var remoteSolvedNotified by remember { mutableStateOf(false) }

    var isSubmitting by remember { mutableStateOf(false) }

    LaunchedEffect(playerId) {
        playerViewModel.loadPlayer(playerId)
    }

    LaunchedEffect(Unit) {
        Log.d("HARD_SCREEN", "Cargando canciones hard")
        songViewModel.loadSongsByLevel("hard")
    }

    val currentSong = songs.getOrNull(index)

    LaunchedEffect(currentSong?.id) {
        currentSong?.let {
            Log.d("HARD_SCREEN", "Observando canción ${it.id}")
            songViewModel.observeSong(it.id)

            // Resetear estados al cambiar de canción
            answer = ""
            feedback = ""
            solved = false
            usedHint1 = false
            usedHint2 = false
            showAlbum = false
            remoteSolvedNotified = false
            isSubmitting = false
        }
    }

    val isSolved = if (realtimeSong?.id == currentSong?.id) {
        realtimeSong?.data?.solved ?: false
    } else {
        false
    }

    LaunchedEffect(isSolved, currentSong?.id) {
        if (isSolved && !solved && !remoteSolvedNotified) {
            feedback = "⚠️ Otro jugador ya ha resuelto esta canción\nRespuesta: ${currentSong?.data?.originalName}"
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
            .padding(24.dp)
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎉 Nivel difícil completado",
                    color = Color.White,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Jugador: ${player?.name ?: ""}",
                    color = Color(0xFFF5E9FF)
                )
                Text(
                    text = "Puntuación: ${player?.score ?: 0}",
                    color = Color(0xFF7CFFB2)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = onLevelCompleted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB48EAD),
                        contentColor = Color.White
                    )
                ) {
                    Text("Continuar al bonus")
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
                    color = Color(0xFFF5E9FF)
                )

                Text(
                    text = "Puntos: ${player?.score ?: 0}",
                    color = Color(0xFF7CFFB2)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = currentSong.data.latinName,
                    color = Color.White,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (showAlbum) {
                    Text(
                        text = "💿 Álbum: ${currentSong.data.album}",
                        color = Color(0xFFFFD166)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Row {
                    Button(
                        enabled = !usedHint1 && !isSolved && !solved && !isSubmitting,
                        onClick = {
                            usedHint1 = true
                            showAlbum = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB48EAD),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Pista 1")
                    }

                    Spacer(modifier = Modifier.width(15.dp))

                    Button(
                        enabled = !usedHint2 && !isSolved && !solved && !isSubmitting,
                        onClick = {
                            usedHint2 = true
                            feedback = "📖 Solicitar diccionario a Julia"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB48EAD),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Pista 2")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    enabled = !isSolved && !solved && !isSubmitting,
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
                        color = if (solved) Color(0xFF7CFFB2) else Color(0xFFFF5D8F),
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Button(
                    onClick = {
                        // Caso 1: Ya resuelta → pasar a la siguiente
                        if (solved || isSolved) {
                            index++
                            answer = ""
                            feedback = ""
                            solved = false
                            usedHint1 = false
                            usedHint2 = false
                            showAlbum = false
                            remoteSolvedNotified = false
                            isSubmitting = false
                            return@Button
                        }

                        // Caso 2: Respuesta vacía
                        if (answer.trim().isEmpty()) {
                            feedback = "✏️ Escribe una respuesta"
                            return@Button
                        }

                        // Caso 3: Comprobar respuesta
                        if (answer.trim().equals(currentSong.data.originalName.trim(), ignoreCase = true)) {
                            if (isSubmitting) return@Button
                            isSubmitting = true

                            val points = when {
                                usedHint2 -> 3
                                usedHint1 -> 4
                                else -> 5
                            }

                            songViewModel.solveSong(currentSong.id) { success ->
                                if (success) {
                                    playerViewModel.addPoints(playerId, points) { pointsAdded ->
                                        if (pointsAdded) {
                                            playerViewModel.loadPlayer(playerId)
                                            feedback = "💖 Correcto (+$points puntos)"
                                            solved = true
                                        } else {
                                            feedback = "⚠️ Error al sumar los puntos"
                                        }
                                        isSubmitting = false
                                    }
                                } else {
                                    feedback = "⚠️ Otro jugador ya lo ha resuelto.\nRespuesta: ${currentSong.data.originalName}"
                                    isSubmitting = false
                                }
                            }
                        } else {
                            feedback = "❌ Respuesta incorrecta"
                        }
                    },
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB48EAD),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        if (solved || isSolved) "Continuar"
                        else if (isSubmitting) "Enviando..."
                        else "Comprobar"
                    )
                }
            }
        }
    }
}