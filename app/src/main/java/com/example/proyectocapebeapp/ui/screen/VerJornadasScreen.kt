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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.ui.viewmodel.InscripcionViewModel
import com.example.proyectocapebeapp.ui.viewmodel.JornadaViewModel

@Composable
fun VerJornadasScreen(
    usuarioId: Int,
    onNavigateToInvitado: () -> Unit,
    viewModel: JornadaViewModel = viewModel(),
    inscripcionViewModel: InscripcionViewModel = viewModel()
) {

    val context = LocalContext.current
    val jornadas = viewModel.jornadas

    LaunchedEffect(Unit) {
        viewModel.cargarJornadas(context)
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

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(text = "Fecha: ${jornada.fecha}")
                            Text(text = "Hora: ${jornada.hora}")
                            Text(text = "Cupos disponibles: ${jornada.cupos}")
                            Text(text = "Estado: ${jornada.estado}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    inscripcionViewModel.inscribirseEnVuelo(
                                        context = context,
                                        usuarioId = usuarioId,
                                        jornadaId = jornada.id
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Inscribirme")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {onNavigateToInvitado()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver")
        }
    }
}