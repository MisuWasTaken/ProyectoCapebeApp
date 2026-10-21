package com.example.proyectocapebeapp.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import com.example.proyectocapebeapp.ui.viewmodel.InscripcionViewModel
import com.example.proyectocapebeapp.ui.viewmodel.JornadaViewModel
private val BluePrimary = Color(0xFF0D47A1)
private val BlueSecondary = Color(0xFF1976D2)
private val BlueBackground = Color(0xFFF4F7FA)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)
private val StatusGreen = Color(0xFF16A34A)
private val StatusRed = Color(0xFFDC2626)
private val StatusOrange = Color(0xFFD97706)

@Composable
fun VerJornadasScreen(
    usuarioId: Int,
    onNavigateToInvitado: () -> Unit,
    viewModel: JornadaViewModel = viewModel(),
    inscripcionViewModel: InscripcionViewModel = viewModel()
) {
    val context = LocalContext.current
    val jornadas = viewModel.jornadas
    var jornadaSeleccionada by remember { mutableStateOf<JornadaEntity?>(null) }
    var consentimientoAceptado by remember { mutableStateOf(false) }

    val permisoNotificaciones = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {}
    LaunchedEffect(Unit) {
        viewModel.cargarJornadas(context)
        inscripcionViewModel.cargarVuelosInscritos(
            context = context,
            usuarioId = usuarioId
        )
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
                    contentDescription = "Vuelos",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Vuelos Disponibles",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
                Text(
                    text = "Selecciona un vuelo para tu inscripcion",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (jornadas.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Sin vuelos",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No hay vuelos disponibles por ahora.",
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
                items(jornadas) { jornada ->
                    val estaInscrito = inscripcionViewModel.vuelosInscritos.contains(jornada.id)
                    val cupos = viewModel.cuposDisponibles[jornada.id] ?: jornada.cupos
                    val sinCupos = cupos <= 0

                    JornadaCardItem(
                        fecha = jornada.fecha,
                        hora = jornada.hora,
                        cupos = cupos,
                        cuposTotales = jornada.cupos,
                        estado = jornada.estado,
                        estaInscrito = estaInscrito,
                        sinCupos = sinCupos,
                        onInscribirClick = {
                            jornadaSeleccionada = jornada
                            consentimientoAceptado = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNavigateToInvitado,
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
                text = "Volver al menú",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
    if (jornadaSeleccionada != null) {
        AlertDialog(
            onDismissRequest = {
                jornadaSeleccionada = null
                consentimientoAceptado = false
            },
            title = {
                Text(
                    text = "Consentimiento de Vuelo",
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            },
            text = {
                Column {
                    Text(
                        text = "Para completar tu inscripcion debes aceptar las condiciones del vuelo recreativo.",
                        color = TextDark,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Declaro que acepto participar en el vuelo recreativo CAPEBE, seguir las indicaciones de seguridad y presentarme al menos 15 minutos antes del horario asignado.",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = consentimientoAceptado,
                            onCheckedChange = { consentimientoAceptado = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Acepto las condiciones",
                            fontSize = 14.sp,
                            color = TextDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = consentimientoAceptado,
                    onClick = {
                        val vuelo = jornadaSeleccionada
                        if (vuelo != null) {
                            inscripcionViewModel.inscribirseEnVuelo(
                                context = context,
                                usuarioId = usuarioId,
                                jornadaId = vuelo.id,
                                onSuccess = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.POST_NOTIFICATIONS
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        permisoNotificaciones.launch(
                                            Manifest.permission.POST_NOTIFICATIONS
                                        )
                                    }
                                    Toast.makeText(
                                        context,
                                        "Te has inscrito correctamente a este vuelo.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    jornadaSeleccionada = null
                                    consentimientoAceptado = false
                                    viewModel.cargarJornadas(context)
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Confirmar inscripcion", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        jornadaSeleccionada = null
                        consentimientoAceptado = false
                    }
                ) {
                    Text(text = "Cancelar", color = TextMuted)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
@Composable
private fun JornadaCardItem(
    fecha: String,
    hora: String,
    cupos: Int,
    cuposTotales: Int,
    estado: String,
    estaInscrito: Boolean,
    sinCupos: Boolean,
    onInscribirClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
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
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                val chipBg = when {
                    estaInscrito -> StatusOrange.copy(alpha = 0.12f)
                    sinCupos -> StatusRed.copy(alpha = 0.12f)
                    else -> StatusGreen.copy(alpha = 0.12f)
                }
                val chipText = when {
                    estaInscrito -> StatusOrange
                    sinCupos -> StatusRed
                    else -> StatusGreen
                }
                val chipLabel = when {
                    estaInscrito -> "Inscrito"
                    sinCupos -> "Agotado"
                    else -> estado.ifEmpty { "Disponible" }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(chipBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = chipLabel,
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
                JornadaInfoRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha",
                    value = fecha
                )
                JornadaInfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Hora",
                    value = hora
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            JornadaInfoRow(
                icon = Icons.Default.EventSeat,
                label = "Disponibilidad",
                value = "$cupos de $cuposTotales cupos libres"
            )

            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onInscribirClick,
                enabled = !estaInscrito && !sinCupos,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueSecondary,
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Text(
                    text = when {
                        estaInscrito -> "Ya estas inscrito"
                        sinCupos -> "Sin cupos disponibles"
                        else -> "Inscribirme al vuelo"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
private fun JornadaInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BlueSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 13.sp,
                color = TextDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}