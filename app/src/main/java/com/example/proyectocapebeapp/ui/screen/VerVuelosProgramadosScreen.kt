package com.example.proyectocapebeapp.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.ui.viewmodel.JornadaViewModel
private val BluePrimary = Color(0xFF0D47A1)
private val BlueSecondary = Color(0xFF1976D2)
private val BlueBackground = Color(0xFFF4F7FA)
private val BlueCardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)
private val StatusGreen = Color(0xFF16A34A)
private val StatusRed = Color(0xFFDC2626)

@Composable
fun VerVuelosProgramadosScreen(
    onNavigateToAdmin: () -> Unit,
    viewModel: JornadaViewModel = viewModel()
) {
    val context = LocalContext.current
    val vuelos = viewModel.jornadas

    LaunchedEffect(Unit) {
        viewModel.cargarJornadas(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FlightTakeoff,
                    contentDescription = "Icono Vuelos",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Vuelos Programados",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
                Text(
                    text = "Consulta el estado y disponibilidad",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (vuelos.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Sin datos",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No hay vuelos programados por ahora.",
                        color = TextMuted,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(vuelos) { vuelo ->
                    val cuposDisponibles =
                        viewModel.cuposDisponibles[vuelo.id] ?: vuelo.cupos
                    vueloItemCard(
                        fecha = vuelo.fecha,
                        hora = vuelo.hora,
                        cuposTotales = vuelo.cupos,
                        cuposDisponibles = cuposDisponibles,
                        estado = vuelo.estado
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNavigateToAdmin,
            colors = ButtonDefaults.buttonColors(
                containerColor = BluePrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Volver al panel principal",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
@Composable
private fun vueloItemCard(
    fecha: String,
    hora: String,
    cuposTotales: Int,
    cuposDisponibles: Int,
    estado: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlueCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VUELO",
                    fontWeight = FontWeight.ExtraBold,
                    color = BlueSecondary,
                    letterSpacing = 1.sp,
                    fontSize = 13.sp
                )
                val isDisponible = cuposDisponibles > 0
                val chipBg = if (isDisponible) StatusGreen.copy(alpha = 0.12f) else StatusRed.copy(alpha = 0.12f)
                val chipText = if (isDisponible) StatusGreen else StatusRed
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(chipBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isDisponible) estado.ifEmpty { "Disponible" } else "Agotado",
                        color = chipText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                infoIconRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha",
                    value = fecha
                )
                infoIconRow(
                    icon = Icons.Default.Schedule,
                    label = "Hora de salida",
                    value = hora
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            infoIconRow(
                icon = Icons.Default.EventSeat,
                label = "Disponibilidad",
                value = "$cuposDisponibles de $cuposTotales cupos libres"
            )
        }
    }
}
@Composable
private fun infoIconRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BlueSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = TextDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}