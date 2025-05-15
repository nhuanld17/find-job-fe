package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.AuthResponse
import com.example.findjob.data.repository.AuthRepository
import com.example.findjob.utils.InfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    public val infoManager: InfoManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                repository.login(email, password)
                    .onSuccess { authResponse ->
                        _loginState.value = LoginState.Success(authResponse)
                    }
                    .onFailure { error ->
                        _loginState.value = LoginState.Error(error.message ?: "Unknown error")
                    }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val authResponse: AuthResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}