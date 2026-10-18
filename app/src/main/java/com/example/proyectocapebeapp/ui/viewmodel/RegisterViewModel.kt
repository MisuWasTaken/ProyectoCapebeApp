package com.example.proyectocapebeapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyectocapebeapp.model.RegisterModel
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.database.AppDatabase
import com.example.proyectocapebeapp.data.local.entity.UsuarioEntity
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel(){
var uiState by mutableStateOf ( RegisterModel())
    private set

    fun onNameChanged(name: String){
        uiState = uiState.copy(name = name, MensajeError = null)
    }

    fun onEmailChanged(email: String){
        uiState = uiState.copy(email = email, MensajeError = null)
    }

fun onPasswordChanged(password: String){
    uiState = uiState.copy(password = password, MensajeError = null)
}

fun onConfirmPasswordChanged(confirmPassword: String){
    uiState = uiState.copy(confirmPassword = confirmPassword, MensajeError = null)
}
    fun onRegisterClick(
        context: Context,
        onInvitadoSuccess: () -> Unit
    ){
    val state = uiState

    if(
        state.name.isBlank() ||
        state.email.isBlank() ||
        state.password.isBlank() ||
        state.confirmPassword.isBlank()
    ){ uiState = uiState.copy(MensajeError = "Ingrese los datos correspondientes")
       return
    }

    if (state.password != state.confirmPassword){
        uiState = uiState.copy(MensajeError = "Las contraseñas no coinciden.")
        return
    }

    //Validaciones correctas:
        viewModelScope.launch {

            val db = AppDatabase.getDatabase(context)
            val user = db.usuarioDao().getUserByEmail(state.email)

            if (user != null) {
                uiState = uiState.copy(
                    MensajeError = "Este correo ya esta registrado."
                )
                return@launch
            }

            val newUser = UsuarioEntity(
                name = state.name,
                email = state.email,
                password = state.password,
                rol = "Invitado"
            )

            db.usuarioDao().registerUser(newUser)
            uiState = uiState.copy(MensajeError = null)
            onInvitadoSuccess()
        }
}
}