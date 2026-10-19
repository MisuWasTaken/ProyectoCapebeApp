package com.example.proyectocapebeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminHomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToCrearJornada: () -> Unit,
    onNavigateToVerVuelos: () -> Unit,
    onNavigateToVerInscritos: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Inicio Administrador",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                onNavigateToCrearJornada()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Programar vuelo")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                onNavigateToVerVuelos()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ver vuelos programados")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                onNavigateToVerInscritos()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ver pasajeros inscritos")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                onNavigateToLogin()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cerrar sesion")
        }
    }
}