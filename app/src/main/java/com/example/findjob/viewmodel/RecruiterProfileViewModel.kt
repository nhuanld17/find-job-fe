package com.example.findjob.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.request.RecruiterRequest
import com.example.findjob.data.model.request.ChangePasswordRequest
import com.example.findjob.data.model.response.RecruiterResponse
import com.example.findjob.data.repository.RecruiterRepository
import com.example.findjob.utils.CloudinaryConfig
import com.example.findjob.utils.InfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileRecruiterState {
    object Idle : ProfileRecruiterState()
    object Loading : ProfileRecruiterState()
    object Success : ProfileRecruiterState()
    object UpdateSuccess : ProfileRecruiterState()
    data class Error(val message: String) : ProfileRecruiterState()
}

sealed class ProfileAvatarState {
    object Idle : ProfileAvatarState()
    object Loading : ProfileAvatarState()
    data class Success(val url: String) : ProfileAvatarState()
    data class Error(val message: String) : ProfileAvatarState()
}

@HiltViewModel
class RecruiterProfileViewModel @Inject constructor(
    private val repository: RecruiterRepository,
    private val cloudinaryConfig: CloudinaryConfig,
    private val infoManager: InfoManager
) : ViewModel() {
    private val _state = MutableStateFlow<ProfileRecruiterState>(ProfileRecruiterState.Idle)
    val state: StateFlow<ProfileRecruiterState> = _state.asStateFlow()

    private val _profile = MutableStateFlow<RecruiterResponse?>(null)
    val profile: StateFlow<RecruiterResponse?> = _profile.asStateFlow()

    private val _avatarState = MutableStateFlow<ProfileAvatarState>(ProfileAvatarState.Idle)
    val avatarState: StateFlow<ProfileAvatarState> = _avatarState

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            _state.value = ProfileRecruiterState.Loading
            repository.getRecruiterProfile()
                .onSuccess { response ->
                    _profile.value = response
                    _state.value = ProfileRecruiterState.Success
                }
                .onFailure { error ->
                    _state.value = ProfileRecruiterState.Error(error.message ?: "Failed to fetch profile")
                }
        }
    }

    fun updateProfile(
        about: String,
        website: String,
        industry: String,
        location: String,
        since: String,
        specialization: String
    ) {
        viewModelScope.launch {
            _state.value = ProfileRecruiterState.Loading
            val request = RecruiterRequest(
                about = about,
                website = website,
                industry = industry,
                location = location,
                since = since,
                specialization = specialization
            )

            repository.updateRecruiterProfile(request)
                .onSuccess {
                    _state.value = ProfileRecruiterState.UpdateSuccess

                    kotlinx.coroutines.delay(2000)
                    getProfile()
                }
                .onFailure { error ->
                    _state.value = ProfileRecruiterState.Error(error.message ?: "Failed to update profile")
                }
        }
    }

    fun changePassword(current: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _state.value = ProfileRecruiterState.Loading
            val request = ChangePasswordRequest(
                current = current,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            )
            repository.changePassword(request)
                .onSuccess {
                    _state.value = ProfileRecruiterState.UpdateSuccess
                }
                .onFailure { error ->
                    _state.value = ProfileRecruiterState.Error(error.message ?: "Failed to change password")
                }
        }
    }

    fun uploadAndChangeAvatar(uri: Uri) {
        _avatarState.value = ProfileAvatarState.Loading
        cloudinaryConfig.uploadImage(
            uri,
            onSuccess = { url ->
                viewModelScope.launch {
                    val result = repository.changeImageProfile(url)
                    if (result.isSuccess) {
                        _avatarState.value = ProfileAvatarState.Success(url)
                        infoManager.updateImage(url)
                    } else {
                        _avatarState.value = ProfileAvatarState.Error(result.exceptionOrNull()?.message ?: "Failed to update avatar")
                    }
                }
            },
            onError = { error ->
                _avatarState.value = ProfileAvatarState.Error(error)
            }
        )
    }

    fun getProfileImage() {
        _avatarState.value = ProfileAvatarState.Loading
        viewModelScope.launch {
            val result = repository.getProfileImage()
            if (result.isSuccess) {
                val url = result.getOrNull() ?: ""
                _avatarState.value = ProfileAvatarState.Success(url)
            } else {
                _avatarState.value = ProfileAvatarState.Error(result.exceptionOrNull()?.message ?: "Failed to get profile image")
            }
        }
    }
}