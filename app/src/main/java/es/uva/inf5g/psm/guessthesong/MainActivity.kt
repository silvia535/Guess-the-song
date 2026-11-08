package es.uva.inf5g.psm.guessthesong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import es.uva.inf5g.psm.guessthesong.ui.theme.GuessTheSongTheme
import es.uva.inf5g.psm.guessthesong.vistas.GuessTheSong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuessTheSongTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GuessTheSong(innerPadding)
                }
            }
        }
    }
}

