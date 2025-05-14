package com.example.findjob.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.request.ApplyJobRequest
import com.example.findjob.data.repository.JobPostRepository
import com.example.findjob.utils.CloudinaryConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class UploadCVViewModel @Inject constructor(
    private val cloudinaryConfig: CloudinaryConfig,
    private val jobPostRepository: JobPostRepository
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadCVState>(UploadCVState.Idle)
    val uploadState: StateFlow<UploadCVState> = _uploadState.asStateFlow()

    fun uploadCV(uri: Uri, jobId: Int) {
        viewModelScope.launch {
            _uploadState.value = UploadCVState.Loading
            try {
                // Upload to Cloudinary
                val cvUrl = suspendCancellableCoroutine<String> { continuation ->
                    cloudinaryConfig.uploadImage(
                        uri = uri,
                        onSuccess = { url ->
                            continuation.resume(url)
                        },
                        onError = { error ->
                            continuation.resumeWithException(Exception(error))
                        }
                    )
                }

                // Apply job after successful upload
                jobPostRepository.applyJob(ApplyJobRequest(jobId, cvUrl)).fold(
                    onSuccess = {
                        _uploadState.value = UploadCVState.Success(cvUrl)
                    },
                    onFailure = { error ->
                        _uploadState.value = UploadCVState.Error(error.message ?: "Failed to apply job")
                    }
                )
            } catch (e: Exception) {
                _uploadState.value = UploadCVState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class UploadCVState {
    object Idle : UploadCVState()
    object Loading : UploadCVState()
    data class Success(val url: String) : UploadCVState()
    data class Error(val message: String) : UploadCVState()
}