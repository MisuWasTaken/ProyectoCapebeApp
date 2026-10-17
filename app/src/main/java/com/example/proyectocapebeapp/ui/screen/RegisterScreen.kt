package com.example.proyectocapebeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    viewmodel: RegisterViewModel = viewModel()
) {
    
    val state = viewModel.uiState
    val context = LocalContext.current
    
    Scaffold(
        topBar = {Header(title="REGISTRO DE USUARIO")},
        bottomBar = {Footer()}
    ) {innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = innerPadding)
            .padding(all = 16.dp)
        horizontalAlignment = Aligment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro de usuario", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = state.email
            onValueChange = {viewModel.onEmailChanged(email = it )},
            label = {Text("Email")},
            singleLine = true,
            keyboardOptions = keyboardOptions(keyboardType = keyboardType.Email),
            
        )
    }
    
    
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Registro")

        Button(
            onClick = { }
        ) {
            Text(text = "Registrarse")
        }

        Button(
            onClick = {
                onNavigateToLogin()
            }
        ) {
            Text(text = "Volver")
        }
    }
}