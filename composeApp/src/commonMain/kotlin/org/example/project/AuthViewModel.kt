package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var userName by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun updateUserName(value: String) {
        userName = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun updateIsLoading(value: Boolean) {
        isLoading = value
    }

    fun signIn(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            updateIsLoading(value = true)
            delay(2000)
            updateIsLoading(value = false)
            if (userName == "test" && password == "123") {
                onSuccess()
            } else {
                onError("Invalid credentials")
            }
        }
    }
}