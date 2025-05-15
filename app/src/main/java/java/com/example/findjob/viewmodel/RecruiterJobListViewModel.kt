package com.example.findjob.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.ListJobResponse
import com.example.findjob.data.repository.JobPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class RecruiterJobListViewModel @Inject constructor(
    private val repository: JobPostRepository
) : ViewModel() {

    private val _state = MutableStateFlow<JobListState>(JobListState.Idle)
    val state: StateFlow<JobListState> = _state.asStateFlow()

    init {
        getJobPosts()
    }

    @SuppressLint("NewApi")
    fun getJobPosts() {
        viewModelScope.launch {
            try {
                _state.value = JobListState.Loading
                repository.getJobPosts()
                    .onSuccess { jobs ->
                        println("Total jobs received: ${jobs.size}")

                        val currentDate = LocalDate.now(ZoneOffset.UTC)

                        val activeJobs = jobs.filter { job ->
                            val expiryDate = try {
                                job.expirateAt?.let {
                                    OffsetDateTime.parse(it).toLocalDate()
                                }
                            } catch (e: Exception) {
                                println("Error parsing date for job ${job.title}: ${e.message}")
                                null
                            }

                            expiryDate?.isAfter(currentDate) ?: true
                        }

                        val expiredJobs = jobs.filter { job ->
                            val expiryDate = try {
                                job.expirateAt?.let {
                                    OffsetDateTime.parse(it).toLocalDate()
                                }
                            } catch (e: Exception) {
                                println("Error parsing date for job ${job.title}: ${e.message}")
                                null
                            }

                            expiryDate?.let { !it.isAfter(currentDate) } ?: false
                        }

                        println("Active jobs: ${activeJobs.size}")
                        println("Expired jobs: ${expiredJobs.size}")

                        _state.value = JobListState.Success(activeJobs, expiredJobs)
                    }
                    .onFailure { error ->
                        println("Error fetching jobs: ${error.message}")
                        _state.value = JobListState.Error(error.message ?: "Failed to fetch jobs")
                    }
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
                _state.value = JobListState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}

sealed class JobListState {
    object Idle : JobListState()
    object Loading : JobListState()
    data class Success(
        val activeJobs: List<ListJobResponse>,
        val expiredJobs: List<ListJobResponse>
    ) : JobListState()
    data class Error(val message: String) : JobListState()
}
