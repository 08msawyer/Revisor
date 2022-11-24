@file:OptIn(ExperimentalMaterial3Api::class)

package com.llamalad7.revisor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.llamalad7.revisor.models.AuthViewModel
import com.llamalad7.revisor.state.RevisorAppState
import com.llamalad7.revisor.ui.theme.RevisorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val snackbarHostState = SnackbarHostState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            RevisorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun AuthScreen(snackbarHostState: SnackbarHostState, viewModel: AuthViewModel = hiltViewModel()) {
    val appState = rememberAppState(snackbarHostState)
    val uiState by viewModel.uiState
    var isLogin by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showToast: (String) -> Unit = { showToast(context, it) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(8.dp),
                snackbar = {
                    Snackbar(it, contentColor = MaterialTheme.colorScheme.onPrimary)
                }
            )
        }
    ) { innerPaddingModifier ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPaddingModifier)
                .fillMaxSize(),
        ) {
            Text("Revisor", fontWeight = FontWeight.Bold, fontSize = 8.em)
            EmailField(value = uiState.email, onNewValue = viewModel::onEmailChange)
            PasswordField(value = uiState.password, onNewValue = viewModel::onPasswordChange)
            if (!isLogin) {
                PasswordField(
                    value = uiState.confirmationPassword,
                    onNewValue = viewModel::onConfirmationPasswordChange,
                    placeholder = "Confirm Password"
                )
            }
            Text(
                if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Log In",
                Modifier.clickable { isLogin = !isLogin }
            )
            if (isLogin) {
                Text("Forgot Password", Modifier.clickable {
                    viewModel.onForgotPasswordClick(showToast)
                })
            }
            Button({
                if (isLogin) {
                    viewModel.onSignInClick(showToast)
                } else {
                    viewModel.onRegisterClick(showToast)
                }
            }) {
                Text(if (isLogin) "Log In" else "Sign Up")
            }
        }
    }
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, coroutineScope) {
    RevisorAppState(snackbarHostState, coroutineScope)
}

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text("Email") },
    )
}

@Composable
fun PasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    placeholder: String = "Password",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(placeholder) },
        visualTransformation = PasswordVisualTransformation(),
    )
}