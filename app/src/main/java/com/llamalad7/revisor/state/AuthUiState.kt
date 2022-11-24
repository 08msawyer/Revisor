package com.llamalad7.revisor.state

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmationPassword: String = "",
)