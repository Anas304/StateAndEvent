package org.example.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthScreenState(
    val userName: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

sealed interface UiEvent{
    data object Idle : UiEvent
    data class EnterUserName(val value: String) : UiEvent
    data class EnterPassword(val value: String) : UiEvent
    data object SignIn : UiEvent
    data object Navigate : UiEvent
    data class ShowSnackBar(val message: String) : UiEvent
}


class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow(AuthScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthScreenState()
    )

    private val _event : Channel<UiEvent> = Channel()
    val event = _event.receiveAsFlow()


    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.EnterPassword -> {
                _state.update { prev->
                    prev.copy(userName = event.value)
                }
            }
            is UiEvent.EnterUserName -> {
                _state.update { prev->
                    prev.copy(password = event.value)
                }
            }
            UiEvent.SignIn -> {
                signIn()
            }
            else -> {
                //TODO handle else case
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _state.update { prev->
                prev.copy(isLoading = true)
            }
            delay(2000L)
            _state.update { prev->
                prev.copy(isLoading = false)
            }
            if (_state.value.userName == "test" && _state.value.password == "123") {
               _event.send(UiEvent.Navigate)
            } else {
                _event.send(UiEvent.ShowSnackBar("Invalid credentials"))
            }
        }
    }
}