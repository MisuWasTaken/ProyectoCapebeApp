package com.example.proyectocapebeapp.model

data class RegisterModel(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val MensajeError: String?= null
)
