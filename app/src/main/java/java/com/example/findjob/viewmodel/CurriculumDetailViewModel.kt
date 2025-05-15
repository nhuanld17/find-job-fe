package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.CvResponse
import com.example.findjob.data.repository.NotificationRepository
import com.example.findjob.data.repository.RecruiterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurriculumDetailViewModel @Inject constructor(
    private val recruiterRepository: RecruiterRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val _cvDetail = MutableStateFlow<CvResponse?>(null)
    val cvDetail: StateFlow<CvResponse?> = _cvDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isSuccess = MutableStateFlow<Boolean?>(null)
    val isSuccess: StateFlow<Boolean?> = _isSuccess.asStateFlow()

//    fun getCvDetail(id: Int) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//
//            try {
//                recruiterRepository.getCvDetail(id).fold(
//                    onSuccess = { cv ->
//                        _cvDetail.value = cv
//                    },
//                    onFailure = { exception ->
//                        _error.value = exception.message ?: "Không thể tải thông tin CV"
//                    }
//                )
//            } catch (e: Exception) {
//                _error.value = "Đã xảy ra lỗi khi tải thông tin CV"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun acceptCv(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _isSuccess.value = null

            try {
                notificationRepository.acceptNotification(id).fold(
                    onSuccess = {
                        _isSuccess.value = true
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Không thể chấp nhận CV"
                    }
                )
            } catch (e: Exception) {
                _error.value = "Đã xảy ra lỗi khi chấp nhận CV"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun rejectCv(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _isSuccess.value = null

            try {
                notificationRepository.rejectNotification(id).fold(
                    onSuccess = {
                        _isSuccess.value = true
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Không thể từ chối CV"
                    }
                )
            } catch (e: Exception) {
                _error.value = "Đã xảy ra lỗi khi từ chối CV"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _isSuccess.value = null
        _error.value = null
    }
} 