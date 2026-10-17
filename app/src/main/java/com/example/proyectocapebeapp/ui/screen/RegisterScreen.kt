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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro de usuario", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = state.email,
            onValueChange = {viewModel.onEmailChanged(email = it )},
            label = {Text("Email")},
            singleLine = true,
            keyboardOptions = keyboardOptions(keyboardType = keyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer (modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChanged(password = it)},
            label = {Text("Contraseña")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = keyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        //Confirmar contraseña
        
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(confirmPassword = it)},
            label = {Text("Repetir Contraseña")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = keyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        
        
        //Error
        
    if (state.MensajeError != null){
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = state.MensajeError,
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium
            )
    }
    
    Spacer(modifier = Modifier.height(20.dp))
    
    Button(
        onClick = {
            viewModel.onRegisterClick{
                Toast.makeText(
                    context,
                    text = "Registro exitosos, inicie sesion",
                    duration = Toast.LENGTH_SHORT
                ).show()
                onNavigateToLogin()
            }
        },
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = "Registrarse")
    }
    Spacer(modifier = Modifier.height(8.dp))
    
    
    
}
}
}