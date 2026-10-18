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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.ui.viewmodel.InscripcionViewModel

@Composable
fun MisVuelosScreen(
    usuarioId: Int,
    onNavigateToInvitado: () -> Unit,
    viewModel: InscripcionViewModel = viewModel()
) {

    val context = LocalContext.current
    val vuelos = viewModel.misVuelos
    LaunchedEffect(Unit) {
        viewModel.cargarMisVuelos(
            context = context,
            usuarioId = usuarioId
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis vuelos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (vuelos.isEmpty()) {
            Text(
                text = "Todavia no estas inscrito en ningun vuelo."
            )
        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(vuelos) { vuelo ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Fecha: ${vuelo.fecha}")
                            Text(text = "Hora: ${vuelo.hora}")
                            Text(text = "Estado: ${vuelo.estado}")
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
}