package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.request.JobPostRequest
import com.example.findjob.data.repository.JobPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class JobPostViewModel @Inject constructor(
    private val repository: JobPostRepository
) : ViewModel() {
    private val _state = MutableStateFlow<JobPostState>(JobPostState.Idle)
    val state: StateFlow<JobPostState> = _state.asStateFlow()

    fun createJobPost(
        title: String,
        description: String,
        requirement: String,
        position: String,
        qualification: String,
        experience: String,
        type: String,
        salary: String,
        workType: String,
        expirationDate: String?
    ) {
        viewModelScope.launch {
            _state.value = JobPostState.Loading
            try {
                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                
                val expirateAt = if (expirationDate != null) {
                    val date = inputFormat.parse(expirationDate)
                    outputFormat.parse(outputFormat.format(date))
                } else {
                    outputFormat.parse("2025-05-30T23:59:59")
                }

                val request = JobPostRequest(
                    title = title,
                    description = description,
                    requirement = requirement,
                    position = position,
                    qualification = qualification,
                    experience = experience,
                    type = type,
                    salary = salary,
                    workplaceType = workType,
                    expirateAt = expirateAt
                )
                
                repository.createJobPost(request)
                _state.value = JobPostState.Success("Job post created successfully!")
            } catch (e: Exception) {
                _state.value = JobPostState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class JobPostState {
    object Idle : JobPostState()
    object Loading : JobPostState()
    data class Success(val message: String) : JobPostState()
    data class Error(val message: String) : JobPostState()
}