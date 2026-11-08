package es.uva.inf5g.psm.guessthesong.vistas

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
fun BonusSongScreen(
    playerId: String,
    onLevelCompleted: () -> Unit
) {

    val songViewModel: SongViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()

    val songs by songViewModel.songs.collectAsState()
    val realtimeSong by songViewModel.currentSong.collectAsState()
    val player by playerViewModel.currentPlayer.collectAsState()
    val loading by songViewModel.loading.collectAsState()
    val errorMessage by songViewModel.message.collectAsState()

    var index by remember { mutableStateOf(0) }
    var answer by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var solved by remember { mutableStateOf(false) }

    var remoteSolvedNotified by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(playerId) {
        playerViewModel.loadPlayer(playerId)
    }

    LaunchedEffect(Unit) {
        songViewModel.loadSongsByLevel("bonus")
    }

    val currentSong = songs.getOrNull(index)

    LaunchedEffect(currentSong?.id) {

        currentSong?.let {

            songViewModel.observeSong(it.id)

            answer = ""
            feedback = ""
            solved = false
            remoteSolvedNotified = false
        }
    }

    // 🔥 FIX: comparar IDs
    val isSolved = if (realtimeSong?.id == currentSong?.id) {
        realtimeSong?.data?.solved ?: false
    } else {
        false
    }

    LaunchedEffect(isSolved, currentSong?.id) {

        if (
            isSolved &&
            !solved &&
            !remoteSolvedNotified
        ) {

            feedback =
                "⚠️ Otro jugador ya ha resuelto esta canción\n" +
                        "La respuesta correcta era: ${currentSong?.data?.originalName}"

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

        when {

            loading -> {

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            errorMessage != null -> {

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Error cargando canciones",
                        color = Color.Red,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = errorMessage ?: "",
                        color = Color.White
                    )
                }
            }

            songs.isEmpty() -> {

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "No hay canciones bonus",
                        color = Color.White,
                        fontSize = 22.sp
                    )
                }
            }

            currentSong == null -> {

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "⭐ Bonus completado",
                        color = Color(0xFFF5E9FF),
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Puntos: ${player?.score ?: 0}",
                        color = Color(0xFFFFD166),
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onLevelCompleted
                    ) {
                        Text("Ver ranking")
                    }
                }
            }

            else -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        "Jugador: ${player?.name ?: ""}",
                        color = Color(0xFFF5E9FF)
                    )

                    Text(
                        "Puntos: ${player?.score ?: 0}",
                        color = Color(0xFF7CFFB2)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = currentSong.data.latinName,
                        color = Color.White,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    OutlinedTextField(
                        value = answer,
                        onValueChange = {
                            answer = it
                        },
                        enabled = !isSolved && !solved,
                        label = {
                            Text("Respuesta")
                        },
                        textStyle = TextStyle(
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (feedback.isNotEmpty()) {

                        Text(
                            text = feedback,
                            color =
                                if (solved)
                                    Color(0xFF7CFFB2)
                                else
                                    Color(0xFFFF5D8F),
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {

                            if (solved || isSolved) {

                                index++

                                answer = ""
                                feedback = ""
                                solved = false

                                return@Button
                            }

                            if (
                                answer.trim().lowercase() ==
                                currentSong.data.originalName
                                    .trim()
                                    .lowercase()
                            ) {

                                songViewModel.solveSong(
                                    currentSong.id
                                ) { success ->

                                    if (success) {

                                        playerViewModel.addPoints(
                                            playerId,
                                            10
                                        )

                                        playerViewModel.loadPlayer(
                                            playerId
                                        )

                                        feedback =
                                            "⭐ Correcto (+10 puntos)"

                                        solved = true

                                    } else {

                                        feedback =
                                            "⚠️ Otro jugador ya lo ha resuelto.\n" +
                                                    "La respuesta correcta era: ${currentSong.data.originalName}"
                                    }
                                }

                            } else {

                                feedback =
                                    "❌ Respuesta incorrecta"
                            }
                        }
                    ) {

                        Text(
                            if (solved || isSolved)
                                "Continuar"
                            else
                                "Comprobar"
                        )
                    }
                }
            }
        }
    }
}