
package com.example.proyectocapebeapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectocapebeapp.model.LoginModel
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.database.AppDatabase
import kotlinx.coroutines.launch

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

    fun onLoginClick(
        context: Context,
        onInvitadoSuccess: (Int) -> Unit,
        onAdminSuccess: () -> Unit
    ) {
        val email = uiState.email.trim()
        val password = uiState.password

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Los campos no pueden estar vacios."
            return
        }

        viewModelScope.launch {
            try {
                val db = AppDatabase.getDatabase(context)
                val user = db.usuarioDao().getUserByEmail(email)

                if (user != null && user.password == password) {

                    if (user.rol.equals("Administrador", ignoreCase = true)) {
                        errorMessage = null
                        onAdminSuccess()

                    } else if (user.rol.equals("Invitado", ignoreCase = true)) {
                        errorMessage = null
                        onInvitadoSuccess(user.id)

                    } else {
                        errorMessage = "Rol de usuario no valido."
                    }

                } else {
                    errorMessage = "Correo o contraseña incorrectos."
                }

            } catch (e: Exception) {
                errorMessage = "No se pudo consultar la base de datos."
            }
        }
    }
}
