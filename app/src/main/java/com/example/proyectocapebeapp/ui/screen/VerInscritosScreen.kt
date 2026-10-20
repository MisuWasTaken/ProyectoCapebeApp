package com.example.proyectocapebeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import com.example.proyectocapebeapp.model.PasajeroInscritoModel
import com.example.proyectocapebeapp.ui.viewmodel.InscripcionViewModel
import com.example.proyectocapebeapp.ui.viewmodel.JornadaViewModel
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun VerInscritosScreen(
    onNavigateToAdmin: () -> Unit,
    jornadaViewModel: JornadaViewModel = viewModel(),
    inscripcionViewModel: InscripcionViewModel = viewModel()
) {
    val context = LocalContext.current
    val vuelos = jornadaViewModel.jornadas
    val pasajeros = inscripcionViewModel.pasajerosInscritos
    var vueloSeleccionado by
    remember { mutableStateOf<JornadaEntity?>(null) }
    var pasajeroSeleccionado by
    remember { mutableStateOf<PasajeroInscritoModel?>(null) }
    LaunchedEffect(Unit) {
        jornadaViewModel.cargarJornadas(context)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gestionar pasajeros",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))
        if (inscripcionViewModel.mensaje != null) {
            Text(
                text = inscripcionViewModel.mensaje!!
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
        val vueloActual = vueloSeleccionado
        val pasajeroActual = pasajeroSeleccionado
        if (vueloActual == null) {
            Text(text = "Selecciona un vuelo")
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                verticalArrangement =
                    Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f))
            {
                items(vuelos) { vuelo ->
                    val cupos =
                        jornadaViewModel
                            .cuposDisponibles[vuelo.id]
                            ?: vuelo.cupos
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Fecha: ${vuelo.fecha}")
                            Text("Hora: ${vuelo.hora}")
                            Text("Cupos disponibles: $cupos")
                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )
                            Button(
                                onClick = {
                                    vueloSeleccionado = vuelo
                                    inscripcionViewModel
                                        .cargarPasajerosInscritos(
                                            context = context,
                                            jornadaId = vuelo.id
                                        ) },
                                modifier =
                                    Modifier.fillMaxWidth())
                            {
                                Text("Ver pasajeros")
                            }
                        }
                    }
                }
            }

        } else if (pasajeroActual == null) {
            Text(text = "Vuelo: ${vueloActual.fecha} - ${vueloActual.hora}")

            Spacer(modifier = Modifier.height(12.dp))
            if (pasajeros.isEmpty()) {
                Text(text = "No hay pasajeros inscritos en este vuelo.")
            } else {
                Text(text = "Pasajeros inscritos: ${pasajeros.size}")

                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(pasajeros) { pasajero ->
                        Card(
                            modifier =
                                Modifier.fillMaxWidth())
                        {
                            Column(
                                modifier =
                                    Modifier.padding(16.dp)
                            ) {
                                val foto =
                                    if (pasajero.fotoPath != null) {
                                        BitmapFactory.decodeFile(
                                            pasajero.fotoPath
                                        )
                                    } else {
                                        null
                                    }
                                if (foto != null) {
                                    Image(
                                        bitmap = foto.asImageBitmap(),
                                        contentDescription =
                                            "Foto de identificacion",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(
                                        modifier =
                                            Modifier.height(8.dp)
                                    )
                                }
                                Text(
                                    text = pasajero.nombre
                                )
                                Text(
                                    text = pasajero.email
                                )
                                Text(
                                    text = if (pasajero.consentimientoAceptado) {
                                        "Consentimiento: Aceptado"
                                    } else {
                                        "Consentimiento: Pendiente"
                                    }
                                )
                                Spacer( modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        pasajeroSeleccionado = pasajero
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text("Cambiar vuelo")
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    vueloSeleccionado = null
                    pasajeroSeleccionado = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a vuelos")
            }
        } else {
            Text(
                text =
                    "Cambiar vuelo de ${pasajeroActual.nombre}"
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =
                    "Vuelo actual: ${vueloActual.fecha} - ${vueloActual.hora}"
            )

            Spacer(modifier = Modifier.height(16.dp))
            var cantidadOpciones = 0
            for (nuevoVuelo in vuelos) {
                val cupos =
                    jornadaViewModel
                        .cuposDisponibles[nuevoVuelo.id]
                        ?: nuevoVuelo.cupos
                if (
                    nuevoVuelo.id != vueloActual.id &&
                    cupos > 0 &&
                    nuevoVuelo.estado == "Disponible" &&
                    inscripcionViewModel.esFechaPosterior(
                        vueloActual.fecha,
                        nuevoVuelo.fecha
                    )
                ) {
                    cantidadOpciones++
                }
            }

            if (cantidadOpciones == 0) {
                Text(
                    text =
                        "No hay vuelos posteriores con cupos disponibles."
                )
            } else {
                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(vuelos) { nuevoVuelo ->
                        val cupos =
                            jornadaViewModel
                                .cuposDisponibles[nuevoVuelo.id]
                                ?: nuevoVuelo.cupos
                        val esValido =
                            nuevoVuelo.id != vueloActual.id &&
                                    cupos > 0 &&
                                    nuevoVuelo.estado == "Disponible" &&
                                    inscripcionViewModel.esFechaPosterior(
                                        vueloActual.fecha,
                                        nuevoVuelo.fecha
                                    )
                        if (esValido) {
                            Card(
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier =
                                        Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        "Fecha: ${nuevoVuelo.fecha}"
                                    )
                                    Text(
                                        "Hora: ${nuevoVuelo.hora}"
                                    )
                                    Text(
                                        "Cupos disponibles: $cupos"
                                    )
                                    Spacer(
                                        modifier =
                                            Modifier.height(8.dp)
                                    )
                                    Button(
                                        onClick = {
                                            inscripcionViewModel
                                                .cambiarVuelo(
                                                    context = context,
                                                    inscripcionId =
                                                        pasajeroActual.inscripcionId,
                                                    usuarioId =
                                                        pasajeroActual.usuarioId,
                                                    nuevaJornadaId =
                                                        nuevoVuelo.id,
                                                    onSuccess = {
                                                        jornadaViewModel
                                                            .cargarJornadas(
                                                                context
                                                            )
                                                        inscripcionViewModel
                                                            .cargarPasajerosInscritos(
                                                                context,
                                                                vueloActual.id
                                                            )
                                                        pasajeroSeleccionado =
                                                            null
                                                    }
                                                )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Cambiar a este vuelo")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick =
                {
                    pasajeroSeleccionado = null
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cancelar cambio") }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onNavigateToAdmin()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}