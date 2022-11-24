package com.llamalad7.revisor.state

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope

class RevisorAppState(
    private val snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    init {
//        coroutineScope.launch {
//            SnackbarManager.snackbarMessages.filterNotNull().collect {
//                snackbarHostState.showSnackbar(it)
//            }
//        }
    }
}