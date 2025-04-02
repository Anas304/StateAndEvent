package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthScreenState(
    val userName: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)
class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow(AuthScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthScreenState()
    )

    fun updateUserName(value: String) {
        _state.update { prev->
            prev.copy(userName = value)
        }
    }

    fun updatePassword(value: String) {
        _state.update { prev->
            prev.copy(password = value)
        }
    }

    fun updateIsLoading(value: Boolean) {
        _state.update {
            it.copy(isLoading = value)
        }
    }

    fun signIn(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            updateIsLoading(value = true)
            delay(2000L)
            updateIsLoading(value = false)
            if (_state.value.userName == "test" && _state.value.password == "123") {
                onSuccess()
            } else {
                onError("Invalid credentials")
            }
        }
    }
}