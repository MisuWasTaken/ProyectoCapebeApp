package com.example.proyectocapebeapp.ui.viewmodel


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
    
}
}