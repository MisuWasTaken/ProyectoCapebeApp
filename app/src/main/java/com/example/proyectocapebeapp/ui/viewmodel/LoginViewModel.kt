package com.example.proyectocapebeapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectocapebeapp.model.LoginModel

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginModel())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email)
        errorMessage = null
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password)
        errorMessage = null
    }

    fun onLoginClick() {
        val email = uiState.email
        val password = uiState.password

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Los campos no pueden estar vacios."
            return
        }
        errorMessage = null
    }
}