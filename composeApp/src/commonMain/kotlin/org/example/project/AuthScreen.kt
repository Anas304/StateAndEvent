package org.example.project

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen() {
    val viewModel = remember { AuthViewModel() }
    val state =  viewModel.state.collectAsState()
    val event =  viewModel.event.collectAsState(UiEvent.Idle)

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(event.value){
        when(event.value){
            is UiEvent.Navigate -> {
                snackBarHostState.showSnackbar("Success!")
            }
            is UiEvent.ShowSnackBar -> {
                snackBarHostState.showSnackbar((event.value as UiEvent.ShowSnackBar).message)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = { TopAppBar(title = { Text(text = "Sign in") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.value.userName,
                onValueChange = { viewModel.onEvent(UiEvent.EnterUserName(it)) },
                label = { Text(text = "UserName") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.value.password,
                onValueChange = { viewModel.onEvent(UiEvent.EnterPassword(it)) },
                label = { Text(text = "Password") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    viewModel.signIn()
                },
                enabled = !state.value.isLoading
            ) {
                AnimatedContent(
                    targetState = state.value.isLoading
                ) { isLoading ->
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Sign in ")
                    }
                }
            }

        }

    }
}