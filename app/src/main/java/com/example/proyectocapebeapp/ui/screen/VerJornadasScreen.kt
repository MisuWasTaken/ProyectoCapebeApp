package com.example.proyectocapebeapp.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import com.example.proyectocapebeapp.ui.viewmodel.InscripcionViewModel
import com.example.proyectocapebeapp.ui.viewmodel.JornadaViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment

@Composable
fun VerJornadasScreen(
    usuarioId: Int,
    onNavigateToInvitado: () -> Unit,
    viewModel: JornadaViewModel = viewModel(),
    inscripcionViewModel: InscripcionViewModel = viewModel()
) {
    val context = LocalContext.current
    val jornadas = viewModel.jornadas
    var jornadaSeleccionada by
    remember { mutableStateOf<JornadaEntity?>(null) }
    var consentimientoAceptado by
    remember { mutableStateOf(false) }
    val permisoNotificaciones =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {
        }
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
            .padding(16.dp)
    ) {
        Text(text = "Vuelos disponibles")
        Spacer(modifier = Modifier.height(16.dp))
        if (jornadas.isEmpty()) {
            Text(text = "No hay vuelos disponibles.")

        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(jornadas) { jornada ->
                    val estaInscrito =
                        inscripcionViewModel.vuelosInscritos.contains(jornada.id)
                    val cupos =
                        viewModel.cuposDisponibles[jornada.id]
                            ?: jornada.cupos
                    val sinCupos = cupos <= 0
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Fecha: ${jornada.fecha}")
                            Text(text = "Hora: ${jornada.hora}")
                            Text(text = "Cupos disponibles: $cupos")
                            Text(text = "Estado: ${jornada.estado}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    jornadaSeleccionada = jornada
                                    consentimientoAceptado = false
                                },
                                enabled = !estaInscrito && !sinCupos,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (estaInscrito) {
                                        "Ya estas inscrito"
                                    } else if (sinCupos) {
                                        "Sin cupos"
                                    } else {
                                        "Inscribirme"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                onNavigateToInvitado()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver")
        }
    }
    if (jornadaSeleccionada != null) {
        AlertDialog(
            onDismissRequest = {
                jornadaSeleccionada = null
                consentimientoAceptado = false
            },
            title = {
                Text(text = "Consentimiento de vuelo")
            },
            text = {
                Column {
                    Text(
                        text = "Para completar tu inscripcion debes aceptar las condiciones del vuelo recreativo."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Declaro que acepto participar en el vuelo recreativo CAPEBE, seguir las indicaciones de seguridad y presentarme al menos 15 minutos antes del horario asignado."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = consentimientoAceptado,
                            onCheckedChange = {
                                consentimientoAceptado = it
                            }
                        )
                        Text(
                            text = "Acepto las condiciones"
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = consentimientoAceptado,
                    onClick = {
                        val vuelo = jornadaSeleccionada
                        if (vuelo != null) {

                            inscripcionViewModel.inscribirseEnVuelo(
                                context = context,
                                usuarioId = usuarioId,
                                jornadaId = vuelo.id,
                                onSuccess = {

                                    if (
                                        Build.VERSION.SDK_INT >=
                                        Build.VERSION_CODES.TIRAMISU &&
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
                    }
                ) {
                    Text(text = "Confirmar inscripcion")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        jornadaSeleccionada = null
                        consentimientoAceptado = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}