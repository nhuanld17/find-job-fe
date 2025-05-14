package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.ListJobResponse
import com.example.findjob.data.repository.JobPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruiterHomeViewModel @Inject constructor(
    private val repository: JobPostRepository
) : ViewModel() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        getRecentJobs()
    }

    fun getRecentJobs() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            repository.getJobPostsRecent()
                .onSuccess { jobs ->
                    _state.value = HomeState.Success(jobs)
                }
                .onFailure { error ->
                    _state.value = HomeState.Error(error.message ?: "Failed to fetch recent jobs")
                }
        }
    }
}

sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Success(val jobs: List<ListJobResponse>) : HomeState()
    data class Error(val message: String) : HomeState()
}