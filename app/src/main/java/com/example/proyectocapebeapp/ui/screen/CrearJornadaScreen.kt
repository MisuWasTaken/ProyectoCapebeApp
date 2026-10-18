package com.example.proyectocapebeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.ui.viewmodel.JornadaViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.material3.OutlinedButton
import java.util.Calendar
import android.widget.Toast

@Composable
fun CrearJornadaScreen(
    onNavigateToAdmin: () -> Unit,
    viewModel: JornadaViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(

        context,
        { _, year, month, dayOfMonth ->
            val fechaSeleccionada =
                "$dayOfMonth/${month + 1}/$year"
            viewModel.onFechaChanged(fechaSeleccionada)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            val horaSeleccionada =
                if (minute < 10) {
                    "$hour:0$minute"
                } else {
                    "$hour:$minute"
                }
            viewModel.onHoraChanged(horaSeleccionada)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Crear Jornada",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                datePickerDialog.show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (state.fecha.isBlank()) {
                    "Seleccionar fecha"
                } else {
                    "Fecha: ${state.fecha}"
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                timePickerDialog.show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (state.hora.isBlank()) {
                    "Seleccionar hora"
                } else {
                    "Hora: ${state.hora}"
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.cupos,
            onValueChange = {
                viewModel.onCuposChanged(it)
            },
            label = {
                Text("Cupos")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.errorMessage,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.onCrearJornadaClick(
                    context = context,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Jornada creada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateToAdmin()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear jornada")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onNavigateToAdmin()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }


    }
}