package com.example.findjob.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.request.ChangePasswordRequest
import com.example.findjob.data.model.response.EmployeeProfileDTO
import com.example.findjob.data.model.response.UpdateEmployeeProfileResponse
import com.example.findjob.data.repository.EmployeeRepository
import com.example.findjob.utils.CloudinaryConfig
import com.example.findjob.utils.InfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeProfileViewModel @Inject constructor(
    private val repository: EmployeeRepository,
    private val cloudinaryConfig: CloudinaryConfig,
    private val infoManager: InfoManager
) : ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    private val _profileImageState = MutableStateFlow<ProfileImageState>(ProfileImageState.Idle)
    val profileImageState: StateFlow<ProfileImageState> = _profileImageState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            repository.getEmployeeProfile()
                .onSuccess { profile ->
                    _state.value = ProfileState.Success(profile)
                }
                .onFailure { error ->
                    _state.value = ProfileState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun updateProfile(profile: EmployeeProfileDTO) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            repository.updateEmployeeProfile(profile)
                .onSuccess { updateResponse ->
                    _state.value = ProfileState.Success(
                        EmployeeProfileDTO(
                            fullName = updateResponse.fullName,
                            email = updateResponse.email,
                            phoneNumber = updateResponse.phoneNumber,
                            dateOfBirth = updateResponse.dateOfBirth,
                            gender = updateResponse.gender,
                            location = updateResponse.location
                        )
                    )
                }
                .onFailure { error ->
                    _state.value = ProfileState.Error(error.message ?: "Failed to update profile")
                }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                _state.value = ProfileState.Loading
                val request = ChangePasswordRequest(
                    current = currentPassword,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
                repository.changePassword(request)
                    .onSuccess {
                        _state.value = ProfileState.PasswordChanged
                    }
                    .onFailure { error ->
                        _state.value = ProfileState.Error(error.message ?: "Failed to change password")
                    }
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun uploadImage(uri: Uri) {
        try {
            viewModelScope.launch {
                try {
                    _uploadState.value = UploadState.Loading
                    cloudinaryConfig.uploadImage(
                        uri = uri,
                        onSuccess = { cloudinaryUrl ->
                            viewModelScope.launch {
                                try {
                                    repository.changeImageProfile(cloudinaryUrl)
                                        .onSuccess { finalUrl -> 
                                            _uploadState.value = UploadState.Success(finalUrl)
                                            infoManager.updateImage(finalUrl)
                                        }
                                        .onFailure { error ->
                                            _uploadState.value = UploadState.Error(error.message ?: "Failed to update avatar")
                                        }
                                } catch (e: Exception) {
                                    _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
                                }
                            }
                        },
                        onError = { error ->
                            _uploadState.value = UploadState.Error(error)
                        }
                    )
                } catch (e: Exception) {
                    _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
                }
            }
        } catch (e: Exception) {
            _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
        }
    }

    fun getProfileImage() {
        viewModelScope.launch {
            try {
                _profileImageState.value = ProfileImageState.Loading
                repository.getProfileImage()
                    .onSuccess { imageUrl ->
                        _profileImageState.value = ProfileImageState.Success(imageUrl)
                    }
                    .onFailure { error ->
                        _profileImageState.value = ProfileImageState.Error(error.message ?: "Failed to get profile image")
                    }
            } catch (e: Exception) {
                _profileImageState.value = ProfileImageState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val profile: EmployeeProfileDTO) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object PasswordChanged : ProfileState()
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Success(val imageUrl: String) : UploadState()
    data class Error(val message: String) : UploadState()
}

sealed class ProfileImageState {
    object Idle : ProfileImageState()
    object Loading : ProfileImageState()
    data class Success(val imageUrl: String) : ProfileImageState()
    data class Error(val message: String) : ProfileImageState()
}