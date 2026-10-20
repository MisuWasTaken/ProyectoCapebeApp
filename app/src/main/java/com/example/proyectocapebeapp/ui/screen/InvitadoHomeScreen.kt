package com.example.proyectocapebeapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta de Colores temáticos para la app de vuelos
private val BluePrimary = Color(0xFF0D47A1)      // Azul Rey
private val BlueSecondary = Color(0xFF1976D2)    // Azul Claro
private val BackgroundLight = Color(0xFFF4F7FA)  // Gris suave de fondo
private val CardBg = Color(0xFFFFFFFF)           // Blanco limpio
private val TextDark = Color(0xFF1E293B)         // Texto principal
private val TextMuted = Color(0xFF64748B)        // Texto secundario
private val DangerRed = Color(0xFFDC2626)        // Rojo para cerrar sesión

@Composable
fun InvitadoHomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToVerJornadas: () -> Unit,
    onNavigateToMisVuelos: () -> Unit,
    onNavigateToFotoIdentificacion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            // Estado de usuario
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(BlueSecondary.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "PASAJERO / INVITADO",
                    color = BluePrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "¡Hola, Viajero!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )
            Text(
                text = "¿Qué deseas realizar en tu vuelo hoy?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextMuted
                )
            )
        }

        // Tarjetas
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Opción 1: Ver Vuelos Disponibles
            MenuOptionCard(
                badgeText = "✈",
                title = "Ver Vuelos Disponibles",
                subtitle = "Explora los horarios y cupos de la agencia",
                onClick = onNavigateToVerJornadas
            )

            // Opción 2: Mis Vuelos
            MenuOptionCard(
                badgeText = "🎫",
                title = "Mis Vuelos",
                subtitle = "Revisa tus reservas e itinerarios activos",
                onClick = onNavigateToMisVuelos
            )

            // Opción 3: Mi Identificación
            MenuOptionCard(
                badgeText = "🪪",
                title = "Mi Identificación",
                subtitle = "Documentos y foto para abordar",
                onClick = onNavigateToFotoIdentificacion
            )
        }

        //Cerrar sesion
        OutlinedButton(
            onClick = onNavigateToLogin,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Cerrar sesión",
                color = DangerRed,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

// tarjeta personalizada
@Composable
private fun MenuOptionCard(
    badgeText: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo con emoji/ícono nativo
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(BluePrimary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeText,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Textos de Título y Subtítulo
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            // Flecha indicadora derecha
            Text(
                text = "›",
                fontSize = 24.sp,
                color = TextMuted,
                fontWeight = FontWeight.Light
            )
        }
    }
}