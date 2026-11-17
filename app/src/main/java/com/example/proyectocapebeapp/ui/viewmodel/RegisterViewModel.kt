
package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.api.RetrofitSupabaseAuth
import com.example.proyectocapebeapp.model.RegisterModel
import com.example.proyectocapebeapp.model.SupabaseRegistroRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {

    var uiState by mutableStateOf(RegisterModel())
        private set

    var cargando by mutableStateOf(false)
        private set

    fun onNameChanged(name: String) {
        uiState = uiState.copy(name = name, MensajeError = null)
    }

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email, MensajeError = null)
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password, MensajeError = null)
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        uiState = uiState.copy(confirmPassword = confirmPassword, MensajeError = null)
    }

    fun onRegisterClick(
        context: Context,
        onInvitadoSuccess: () -> Unit
    ) {
        val state = uiState

        if (cargando) return

        if (
            state.name.isBlank() ||
            state.email.isBlank() ||
            state.password.isBlank() ||
            state.confirmPassword.isBlank()
        ) {
            uiState = state.copy(
                MensajeError = "Ingrese los datos correspondientes."
            )
            return
        }

        if (state.password != state.confirmPassword) {
            uiState = state.copy(
                MensajeError = "Las contraseñas no coinciden."
            )
            return
        }

        if (state.password.length < 6) {
            uiState = state.copy(
                MensajeError = "La contraseña debe tener al menos 6 caracteres."
            )
            return
        }

        viewModelScope.launch {
            cargando = true
            uiState = state.copy(MensajeError = null)

            try {
                RetrofitSupabaseAuth.api.registrar(
                    datos = SupabaseRegistroRequest(
                        email = state.email.trim(),
                        password = state.password,
                        data = mapOf("name" to state.name.trim())
                    )
                )

                onInvitadoSuccess()


            } catch (e: HttpException) {
                val detalle = try {
                    val respuesta = e.response()?.errorBody()?.string() ?: ""
                    val json = org.json.JSONObject(respuesta)

                    json.optString("msg").ifBlank {
                        json.optString("message")
                    }.ifBlank {
                        json.optString("error_description")
                    }.ifBlank {
                        json.optString("error_code")
                    }

                } catch (ex: Exception) {
                    ""
                }

                uiState = uiState.copy(
                    MensajeError = if (detalle.isNotBlank()) {
                        detalle
                    } else {
                        "Error de Supabase: ${e.code()}"
                    }
                )
            }

        }
    }
}
