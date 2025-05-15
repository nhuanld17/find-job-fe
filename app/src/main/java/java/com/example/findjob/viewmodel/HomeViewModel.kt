package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.request.SaveJobRequest
import com.example.findjob.data.model.response.NewestJobResponse
import com.example.findjob.data.repository.JobPostRepository
import com.example.findjob.utils.InfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val infoManager: InfoManager,
    private val jobPostRepository: JobPostRepository
) : ViewModel() {
    private val _userName = MutableStateFlow(infoManager.getName() ?: "")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userImageUrl = MutableStateFlow(infoManager.getImageUrl())
    val userImageUrl: StateFlow<String?> = _userImageUrl.asStateFlow()

    private val _newestJobs = MutableStateFlow<List<NewestJobResponse>>(emptyList())
    val newestJobs: StateFlow<List<NewestJobResponse>> = _newestJobs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    fun getNewestJobsPosts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                jobPostRepository.getNewestJobsPosts()
                    .onSuccess { jobs ->
                        _newestJobs.value = jobs
                    }
                    .onFailure { exception ->
                        _error.value = exception.message ?: "Failed to fetch newest jobs"
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveJob(jobId: Int) {
        viewModelScope.launch {
            try {
                _isSaving.value = true
                _saveError.value = null

                val request = SaveJobRequest(jobId = jobId)

                jobPostRepository.saveJob(request)
                    .onSuccess { saveStatus ->
                        if (saveStatus.isJobSaved) {
                            kotlinx.coroutines.delay(500)
                            getNewestJobsPosts()
                        } else {
                            kotlinx.coroutines.delay(500)
                            getNewestJobsPosts()
                        }
                    }
                    .onFailure { exception ->
                        _saveError.value = exception.message ?: "Failed to save job"
                    }
            } catch (e: Exception) {
                _saveError.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isSaving.value = false
            }
        }
    }
} 