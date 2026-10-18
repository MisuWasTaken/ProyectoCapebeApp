package com.example.proyectocapebeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectocapebeapp.ui.theme.PurpleGrey40

@Composable
fun Footer(
    copyrightText: String = "© 2026 CAPEBE. Todos los derechos reservados.",
    backgroundColor: Color = PurpleGrey40,
    textColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = copyrightText,
            color = textColor,
            fontSize = 12.sp
        )
    }
}