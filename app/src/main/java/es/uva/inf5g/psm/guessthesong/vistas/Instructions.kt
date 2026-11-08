package es.uva.inf5g.psm.guessthesong.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Instructions(
    onNavigateToRegister: () -> Unit = {}
) {

    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1B2E), // noche
            Color(0xFF3B2F4A), // violeta oscuro
            Color(0xFFB48EAD)  // lavanda suave
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .clickable { onNavigateToRegister() },
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 28.dp)
        ) {

            Text(
                text = "Cómo jugar",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5D1E0)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "A lo largo del juego recorrerás tres niveles:",
                fontSize = 20.sp,
                color = Color(0xFFE9D7FF)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "• Fácil\n• Medio\n• Difícil",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFFE4F1),
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "En cada nivel, el primero en acertar la canción gana puntos.",
                fontSize = 20.sp,
                color = Color(0xFFE9D7FF),
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡BUENA SUERTE!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD6E8)
            )

            Spacer(modifier = Modifier.height(28.dp))
//            Text(
//                text = "Pulsa para empezar",
//                fontSize = 18.sp,
//                color = Color(0xFFEEE6FF)
//            )

        }
    }
}