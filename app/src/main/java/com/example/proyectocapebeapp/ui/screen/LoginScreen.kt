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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.ui.viewmodel.LoginViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onInvitadoSuccess: () -> Unit,
    onAdminSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ingreso",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = state.email,
            onValueChange = {
                viewModel.onEmailChanged(it)
            },
            label = {
                Text("Correo electronico")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = {
                viewModel.onPasswordChanged(it)
            },
            label = {
                Text("Contraseña")
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.errorMessage!!,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.onLoginClick(
                    context = context,
                    onInvitadoSuccess = onInvitadoSuccess,
                    onAdminSuccess = onAdminSuccess
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesion")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onNavigateToRegister()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}