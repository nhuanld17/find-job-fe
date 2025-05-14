package com.example.findjob.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.CVAnalysisResponse
import com.example.findjob.data.repository.AIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AIState {
    object Idle : AIState()
    object Loading : AIState()
    data class Success(val analysis: CVAnalysisResponse) : AIState()
    data class Error(val message: String) : AIState()
}

@HiltViewModel
class AIViewModel @Inject constructor(
    private val repository: AIRepository
) : ViewModel() {
    private val _state = MutableStateFlow<AIState>(AIState.Idle)
    val state: StateFlow<AIState> = _state.asStateFlow()

    fun setError(message: String) {
        _state.value = AIState.Error(message)
    }

    fun analyzeCV(uri: Uri) {
        viewModelScope.launch {
            _state.value = AIState.Loading
            repository.analyzeCV(uri)
                .onSuccess { analysis ->
                    _state.value = AIState.Success(analysis)
                }
                .onFailure { error ->
                    _state.value = AIState.Error(error.message ?: "Failed to analyze CV")
                }
        }
    }
} 