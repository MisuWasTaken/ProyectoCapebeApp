package com.example.proyectocapebeapp.model

data class RegisterModel(
    val email: String = "",
    val password: String = "",
    val confirmPasword: String = "",
    val MensajeError: String?= null
)
