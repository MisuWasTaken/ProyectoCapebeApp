package com.example.proyectocapebeapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RegisterViewModel : ViewModel(){
var uiState by mutableStateOf ( RegisterModel())
    private set  
    
fun onEmailChanged(email: String){
    uiState = uiState.copy(email = email, MensajeError = null)
    }
    
fun onPasswordChanged(password: String){
    uiState = uiState.copy(password = password, MensajeError = null)
}

fun onConfirmPasswordChanged(confirmPasword: String){
    uiState = uiState.copy(confirmPasword = confirmPassword, MensajeError = null)  
}

fun onRegisterClick(onInvitadoSuccess: () -> Unit){
    val state = uiState
    
    if(state.email.isBlank() || state.password.isBlank()){
       uiState = uiState.copy(MensajeError = "Ingrese los datos correspondientes")
       return 
    }
    
    if (state.password != state.confirmPassword){
        uiState = uiState.copy(MensajeError = "Las contraseñas no coinciden.")
        return
    }
    
    //Validaciones correctas:
    Log.d(tag= "RegisterVM", msg="Usuario registrado: ${state.name} (${state.email})")
    uiState = uiState.copy(MensajeError = null)
    onSucess()
}
}