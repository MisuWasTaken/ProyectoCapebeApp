package com.example.proyectocapebeapp.ui.screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import com.example.proyectocapebeapp.model.PasajeroInscritoModel
import com.example.proyectocapebeapp.ui.viewmodel.InscripcionViewModel
import com.example.proyectocapebeapp.ui.viewmodel.JornadaViewModel
private val BluePrimary = Color(0xFF0D47A1)
private val BlueSecondary = Color(0xFF1976D2)
private val BlueBackground = Color(0xFFF4F7FA)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)
private val StatusGreen = Color(0xFF16A34A)
private val StatusOrange = Color(0xFFD97706)

@Composable
fun VerInscritosScreen(
    onNavigateToAdmin: () -> Unit,
    jornadaViewModel: JornadaViewModel = viewModel(),
    inscripcionViewModel: InscripcionViewModel = viewModel()
) {
    val context = LocalContext.current
    val vuelos = jornadaViewModel.jornadas
    val pasajeros = inscripcionViewModel.pasajerosInscritos
    var vueloSeleccionado by remember { mutableStateOf<JornadaEntity?>(null) }
    var pasajeroSeleccionado by remember { mutableStateOf<PasajeroInscritoModel?>(null) }

    LaunchedEffect(Unit) {
        jornadaViewModel.cargarJornadas(context)
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
                    contentDescription = "Gestion",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Pasajeros inscritos",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
                Text(
                    text = "Consulta y gestiona los pasajeros inscritos",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (inscripcionViewModel.mensaje != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = BlueSecondary.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = inscripcionViewModel.mensaje!!,
                    color = BluePrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        val vueloActual = vueloSeleccionado
        val pasajeroActual = pasajeroSeleccionado
        when {
            vueloActual == null -> {
                Text(
                    text = "Selecciona un vuelo para ver sus pasajeros",
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(vuelos) { vuelo ->
                        val cupos = jornadaViewModel.cuposDisponibles[vuelo.id] ?: vuelo.cupos
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBg),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    InfoRowSimple(Icons.Default.CalendarToday, "Fecha", vuelo.fecha)
                                    InfoRowSimple(Icons.Default.Schedule, "Hora", vuelo.hora)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                InfoRowSimple(Icons.Default.EventSeat, "Cupos Libres", "$cupos / ${vuelo.cupos}")
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        vueloSeleccionado = vuelo
                                        inscripcionViewModel.cargarPasajerosInscritos(
                                            context = context,
                                            jornadaId = vuelo.id
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Ver pasajeros inscritos", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            pasajeroActual == null -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Vuelo: ${vueloActual.fecha} - ${vueloActual.hora}",
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Pasajeros inscritos: ${pasajeros.size}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (pasajeros.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Info, null, tint = TextMuted, modifier = Modifier.size(44.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No hay pasajeros inscritos en este vuelo.", color = TextMuted)
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(pasajeros) { pasajero ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = CardBg),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val foto = pasajero.fotoPath?.let { BitmapFactory.decodeFile(it) }
                                    if (foto != null) {
                                        Image(
                                            bitmap = foto.asImageBitmap(),
                                            contentDescription = "Foto de identificacion",
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                                .background(BlueSecondary.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Person, null, tint = BlueSecondary)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = pasajero.nombre,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = pasajero.email,
                                            color = TextMuted,
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val consentText = if (pasajero.consentimientoAceptado) "Consentimiento: Aceptado" else "Consentimiento: Pendiente"
                                        val consentColor = if (pasajero.consentimientoAceptado) StatusGreen else StatusOrange
                                        Text(
                                            text = consentText,
                                            color = consentColor,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OutlinedButton(
                                            onClick = { pasajeroSeleccionado = pasajero },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Cambiar vuelo", fontSize = 12.sp, color = BluePrimary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        vueloSeleccionado = null
                        pasajeroSeleccionado = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a selección de vuelos")
                }
            }
            else -> {
                Text(
                    text = "Cambiar vuelo de: ${pasajeroActual.nombre}",
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "Vuelo actual: ${vueloActual.fecha} - ${vueloActual.hora}",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
                val vuelosValidos = vuelos.filter { nuevoVuelo ->
                    val cupos = jornadaViewModel.cuposDisponibles[nuevoVuelo.id] ?: nuevoVuelo.cupos
                    nuevoVuelo.id != vueloActual.id &&
                            cupos > 0 &&
                            nuevoVuelo.estado == "Disponible" &&
                            inscripcionViewModel.esFechaPosterior(vueloActual.fecha, vueloActual.hora,
                                nuevoVuelo.fecha,
                                nuevoVuelo.hora
                            )
                }
                if (vuelosValidos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay vuelos posteriores con cupos disponibles.",
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(vuelosValidos) { nuevoVuelo ->
                            val cupos = jornadaViewModel.cuposDisponibles[nuevoVuelo.id] ?: nuevoVuelo.cupos
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = CardBg),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        InfoRowSimple(Icons.Default.CalendarToday, "Fecha", nuevoVuelo.fecha)
                                        InfoRowSimple(Icons.Default.Schedule, "Hora", nuevoVuelo.hora)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    InfoRowSimple(Icons.Default.EventSeat, "Cupos Libres", "$cupos cupos")
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            inscripcionViewModel.cambiarVuelo(
                                                context = context,
                                                inscripcionId = pasajeroActual.inscripcionId,
                                                usuarioId = pasajeroActual.usuarioId,
                                                nuevaJornadaId = nuevoVuelo.id,
                                                onSuccess = {
                                                    jornadaViewModel.cargarJornadas(context)
                                                    inscripcionViewModel.cargarPasajerosInscritos(
                                                        context = context,
                                                        jornadaId = vueloActual.id
                                                    )
                                                    pasajeroSeleccionado = null
                                                }
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusGreen),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Confirmar cambio a este vuelo", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { pasajeroSeleccionado = null },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar cambio", color = TextMuted)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onNavigateToAdmin,
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver al panel principal", fontWeight = FontWeight.Bold)
        }
    }
}
@Composable
private fun InfoRowSimple(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = BlueSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(text = label, fontSize = 10.sp, color = TextMuted)
            Text(text = value, fontSize = 13.sp, color = TextDark, fontWeight = FontWeight.SemiBold)
        }
    }
}