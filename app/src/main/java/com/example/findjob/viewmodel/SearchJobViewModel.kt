package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.request.Filter
import com.example.findjob.data.model.request.SaveJobRequest
import com.example.findjob.data.model.response.JobSearchResponse
import com.example.findjob.data.repository.JobPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchJobState {
    object Initial : SearchJobState()
    object Loading : SearchJobState()
    data class Success(
        val jobs: List<JobSearchResponse>,
        val currentFilter: Filter
    ) : SearchJobState()
    data class Error(val message: String) : SearchJobState()
}

sealed class SaveJobState {
    object Initial : SaveJobState()
    object Loading : SaveJobState()
    data class Success(val isSaved: Boolean) : SaveJobState()
    data class Error(val message: String) : SaveJobState()
}

@HiltViewModel
class SearchJobViewModel @Inject constructor(
    private val jobPostRepository: JobPostRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchJobState>(SearchJobState.Initial)
    val state: StateFlow<SearchJobState> = _state.asStateFlow()

    private val _saveState = MutableStateFlow<SaveJobState>(SaveJobState.Initial)
    val saveState: StateFlow<SaveJobState> = _saveState.asStateFlow()

    fun searchWith(filter: Filter) {
        viewModelScope.launch {
            try {
                _state.value = SearchJobState.Loading
                
                jobPostRepository.searchWith(filter)
                    .onSuccess { results ->
                        println("Search results in ViewModel: ${results.map { it.saved }}")
                        println("Search results details: ${results.map { "${it.jobTitle} - isSaved: ${it.saved}" }}")
                        _state.value = SearchJobState.Success(results, filter)
                    }
                    .onFailure { exception ->
                        _state.value = SearchJobState.Error(
                            exception.message ?: "Failed to search jobs"
                        )
                    }
            } catch (e: Exception) {
                _state.value = SearchJobState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun saveJob(jobId: Int) {
        viewModelScope.launch {
            try {
                _saveState.value = SaveJobState.Loading

                val request = SaveJobRequest(jobId = jobId)
                jobPostRepository.saveJob(request)
                    .onSuccess { saveStatus ->
                        // Update UI immediately
                        _saveState.value = SaveJobState.Success(saveStatus.isJobSaved)
                        
                        // Refresh search results after successful save/unsave
                        if (state.value is SearchJobState.Success) {
                            val currentFilter = (state.value as SearchJobState.Success).currentFilter
                            searchWith(currentFilter)
                        }
                    }
                    .onFailure { exception ->
                        _saveState.value = SaveJobState.Error(
                            exception.message ?: "Failed to save job"
                        )
                    }
            } catch (e: Exception) {
                _saveState.value = SaveJobState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun resetState() {
        _state.value = SearchJobState.Initial
        _saveState.value = SaveJobState.Initial
    }
}