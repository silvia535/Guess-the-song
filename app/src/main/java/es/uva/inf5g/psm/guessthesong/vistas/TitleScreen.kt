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
fun TitleScreen(
    onNavigateToInstructions: () -> Unit
) {

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1B2E), // azul noche (Midnights)
            Color(0xFF3B2F4A), // morado oscuro
            Color(0xFFB48EAD)  // lavanda suave
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .clickable { onNavigateToInstructions() },
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "TAYLOR latina",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5D1E0) // rosa pastel elegante
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pulsa para empezar",
                fontSize = 26.sp,
                color = Color(0xFFE9D7FF) // lavanda claro
            )
        }
    }
}