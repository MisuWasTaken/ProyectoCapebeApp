
package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.api.RetrofitSupabaseAuth
import com.example.proyectocapebeapp.data.api.SesionSupabase
import com.example.proyectocapebeapp.data.database.AppDatabase
import com.example.proyectocapebeapp.data.local.entity.UsuarioEntity
import com.example.proyectocapebeapp.model.LoginModel
import com.example.proyectocapebeapp.model.SupabaseAuthRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginModel())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var cargando by mutableStateOf(false)
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

        if (cargando) return

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Los campos no pueden estar vacios."
            return
        }

        viewModelScope.launch {
            cargando = true
            errorMessage = null

            try {
                val respuesta = RetrofitSupabaseAuth.api.iniciarSesion(
                    datos = SupabaseAuthRequest(email, password)
                )

                val token = respuesta.accessToken
                val cuenta = respuesta.user
                val refreshToken = respuesta.refreshToken
                    ?: throw IllegalStateException("Sesion no valida.")

                if (token.isNullOrBlank() || cuenta == null) {
                    errorMessage = "No se pudo iniciar sesion."
                    return@launch
                }

                val perfiles = RetrofitSupabaseAuth.api.obtenerPerfil(
                    autorizacion = "Bearer $token",
                    usuarioId = "eq.${cuenta.id}"
                )

                val perfil = perfiles.firstOrNull()

                if (perfil == null) {
                    errorMessage = "No se encontro el perfil del usuario."
                    return@launch
                }

                when (perfil.rol) {

                    "Administrador" -> {
                        SesionSupabase.iniciar(
                            token,
                            cuenta.id,
                            refreshToken,
                            respuesta.expiresIn
                        )
                        onAdminSuccess()
                    }

                    "Invitado" -> {
                        val db = AppDatabase.getDatabase(context)
                        val correo = cuenta.email ?: email

                        var usuario = db.usuarioDao().getUserByEmail(correo)

                        if (usuario == null) {
                            val nuevoUsuario = UsuarioEntity(
                                name = perfil.nombre,
                                email = correo,
                                password = "",
                                rol = "Invitado"
                            )

                            db.usuarioDao().registerUser(nuevoUsuario)
                            usuario = db.usuarioDao().getUserByEmail(correo)
                        }

                        if (usuario != null) {
                            SesionSupabase.iniciar(
                                token,
                                cuenta.id,
                                refreshToken,
                                respuesta.expiresIn
                            )
                            onInvitadoSuccess(usuario.id)
                        } else {
                            errorMessage = "No se pudo cargar el usuario."
                        }
                    }

                    else -> {
                        errorMessage = "Rol de usuario no valido."
                    }
                }

            } catch (e: HttpException) {
                errorMessage = when (e.code()) {
                    400, 401 -> "Correo o contrasena incorrectos."
                    429 -> "Demasiados intentos. Intenta nuevamente."
                    else -> "Error al iniciar sesion: ${e.code()}"
                }

            } catch (e: Exception) {
                errorMessage = "No se pudo conectar con Supabase."

            } finally {
                cargando = false
            }
        }
    }
}
