package ru.alex0d.authservice.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val tg: String?
)

data class AuthResponse(
    val token: String,
    val type: String = "Bearer"
)