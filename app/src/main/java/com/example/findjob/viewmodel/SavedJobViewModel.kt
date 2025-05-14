package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.request.SaveJobRequest
import com.example.findjob.data.model.response.SavedJob
import com.example.findjob.data.repository.JobPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedJobViewModel @Inject constructor(
    private val jobPostRepository: JobPostRepository
) : ViewModel() {
    private val _state = MutableStateFlow<SavedJobState>(SavedJobState.Idle)
    val state: StateFlow<SavedJobState> = _state.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    fun getSavedJobs() {
        viewModelScope.launch {
            _state.value = SavedJobState.Loading

            jobPostRepository.getSavedJobs()
                .onSuccess { jobs ->
                    _state.value = SavedJobState.Success(jobs)
                }
                .onFailure { exception ->
                    _state.value = SavedJobState.Error(exception.message ?: "Failed to fetch saved jobs")
                }
        }
    }

    fun saveJob(jobId: Int) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null

            val request = SaveJobRequest(jobId = jobId)
            jobPostRepository.saveJob(request)
                .onSuccess { saveStatus ->
                    // Refresh saved jobs list after successful save/unsave
                    getSavedJobs()
                }
                .onFailure { exception ->
                    _saveError.value = exception.message ?: "Failed to save job"
                }

            _isSaving.value = false
        }
    }
}

sealed class SavedJobState {
    object Idle : SavedJobState()
    object Loading : SavedJobState()
    data class Success(val jobs: List<SavedJob>) : SavedJobState()
    data class Error(val message: String) : SavedJobState()
}