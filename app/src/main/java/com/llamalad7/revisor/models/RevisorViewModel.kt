package com.llamalad7.revisor.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class RevisorViewModel : ViewModel() {
    fun launchCatching(callback: (String) -> Unit, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                callback(throwable.toString())
            },
            block = block
        )
}