package com.example.findjob.data.model.response

data class AuthResponse(
    val email: String,
    val name: String,
    val role: String,
    val imageUrl: String,
    val token: String
)
