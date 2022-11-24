package com.llamalad7.revisor.models

import androidx.compose.runtime.mutableStateOf
import com.llamalad7.revisor.common.isValidEmail
import com.llamalad7.revisor.services.AccountService
import com.llamalad7.revisor.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val accountService: AccountService
) : RevisorViewModel() {
    var uiState = mutableStateOf(AuthUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    private val confirmationPassword
        get() = uiState.value.confirmationPassword

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onConfirmationPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(confirmationPassword = newValue)
    }

    fun onSignInClick(showMessage: (String) -> Unit) {
        if (!email.isValidEmail()) {
            showMessage("Invalid email")
            return
        }

        if (password.isBlank()) {
            showMessage("Invalid password")
            return
        }

        launchCatching(showMessage) {
            accountService.authenticate(email, password)
            showMessage("Successfully logged in as $email")
        }
    }

    fun onRegisterClick(showMessage: (String) -> Unit) {
        if (!email.isValidEmail()) {
            showMessage("Invalid email")
            return
        }

        if (password.isBlank()) {
            showMessage("Invalid password")
            return
        }

        if (password != confirmationPassword) {
            showMessage("Passwords must match!")
            return
        }

        launchCatching(showMessage) {
            accountService.register(email, password)
        }
    }

    fun onForgotPasswordClick(showMessage: (String) -> Unit) {
        if (!email.isValidEmail()) {
            showMessage("Invalid email")
            return
        }

        launchCatching(showMessage) {
            accountService.sendRecoveryEmail(email)
            showMessage("Recovery email sent")
        }
    }
}