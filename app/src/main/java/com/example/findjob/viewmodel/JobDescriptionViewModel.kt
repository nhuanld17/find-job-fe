package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.JobDetailResponse
import com.example.findjob.data.repository.JobPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDescriptionViewModel @Inject constructor(
    private val repository: JobPostRepository
) : ViewModel() {
    private val _jobDetail = MutableStateFlow<JobDetailResponse?>(null)
    val jobDetail: StateFlow<JobDetailResponse?> = _jobDetail

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchJobDetail(id: Int) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = repository.getJobPostDetail(id)
            result.onSuccess {
                _jobDetail.value = it
                _loading.value = false
            }.onFailure { e ->
                _error.value = e.message
                _loading.value = false
            }
        }
    }
}