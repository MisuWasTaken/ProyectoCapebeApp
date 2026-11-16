
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
import androidx.compose.material.icons.filled.FlightTakeoff
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.ui.viewmodel.EmbarqueViewModel

private val BluePrimary = Color(0xFF0D47A1)
private val BlueSecondary = Color(0xFF1976D2)
private val BlueBackground = Color(0xFFF4F7FA)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)
private val StatusGreen = Color(0xFF16A34A)
private val StatusRed = Color(0xFFDC2626)

@Composable
fun RegistrarEmbarqueScreen(
    onNavigateToAdmin: () -> Unit,
    viewModel: EmbarqueViewModel = viewModel()
) {
    val vuelos = viewModel.vuelos.sortedByDescending { it.id }
    val vueloSeleccionado = viewModel.vueloSeleccionado
    val pasajeros = viewModel.pasajeros

    LaunchedEffect(Unit) {
        viewModel.cargarVuelos()
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
                    text = "Control de Participacion",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )

                Text(
                    text = "Registra la participacion de los pasajeros",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.mensaje != null) {
            Text(
                text = viewModel.mensaje ?: "",
                color = if (
                    viewModel.mensaje == "Control iniciado correctamente." ||
                    viewModel.mensaje == "Participacion actualizada correctamente."
                ) StatusGreen else StatusRed,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (vueloSeleccionado == null) {
            Text(
                text = "Selecciona un vuelo",
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.cargando) {
                Text("Cargando vuelos...", color = TextMuted)
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(vuelos) { vuelo ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardBg
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            val partes = vuelo.fecha.split("-")
                            val fecha = if (partes.size == 3) {
                                "${partes[2]}/${partes[1]}/${partes[0]}"
                            } else {
                                vuelo.fecha
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = BlueSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(fecha, color = TextDark)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = BlueSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(
                                        vuelo.hora.take(5),
                                        color = TextDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${vuelo.origen} - ${vuelo.destino}",
                                color = TextMuted,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    viewModel.seleccionarVuelo(vuelo)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BlueSecondary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Ver pasajeros",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "Vuelo: ${vueloSeleccionado.fecha} - ${vueloSeleccionado.hora.take(5)}",
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.cargando) {
                Text(
                    text = "Cargando pasajeros...",
                    color = TextMuted
                )
            }

            if (pasajeros.isEmpty() && !viewModel.cargando) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pasajeros inscritos en este vuelo.",
                        color = TextMuted
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(pasajeros) { pasajero ->
                        val embarque = viewModel.embarques.firstOrNull {
                            it.inscripcionId == pasajero.id
                        }

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CardBg
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = BlueSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
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
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                if (embarque == null) {
                                    Text(
                                        text = "Estado: Sin registro",
                                        color = TextMuted,
                                        fontSize = 12.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Button(
                                        onClick = {
                                            viewModel.registrarEmbarque(pasajero)
                                        },
                                        enabled = !viewModel.registrando &&
                                                !viewModel.cargando,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = BluePrimary
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Iniciar control",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    val estadoTexto = when (embarque.estado) {
                                        "PENDIENTE" -> "Pendiente"
                                        "EMBARCADO" -> "Asistio"
                                        "NO_EMBARCADO" -> "No asistio"
                                        else -> embarque.estado
                                    }

                                    val estadoColor = when (embarque.estado) {
                                        "EMBARCADO" -> StatusGreen
                                        "NO_EMBARCADO" -> StatusRed
                                        else -> TextMuted
                                    }

                                    Text(
                                        text = "Estado: $estadoTexto",
                                        color = estadoColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = {
                                                viewModel.cambiarEstado(
                                                    embarque,
                                                    "EMBARCADO"
                                                )
                                            },
                                            enabled = !viewModel.registrando &&
                                                    embarque.estado != "EMBARCADO",
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = StatusGreen
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                "Participó",
                                                fontSize = 12.sp
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.cambiarEstado(
                                                    embarque,
                                                    "NO_EMBARCADO"
                                                )
                                            },
                                            enabled = !viewModel.registrando &&
                                                    embarque.estado != "NO_EMBARCADO",
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = StatusRed
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                "No participó",
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    viewModel.volverAVuelos()
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Volver a vuelos",
                    color = BluePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onNavigateToAdmin,
            colors = ButtonDefaults.buttonColors(
                containerColor = BluePrimary
            ),
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

            Text(
                text = "Volver al panel principal",
                fontWeight = FontWeight.Bold
            )
        }
    }
}