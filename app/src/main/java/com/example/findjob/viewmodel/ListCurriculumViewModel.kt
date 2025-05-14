package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.CvResponse
import com.example.findjob.data.repository.RecruiterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCurriculumViewModel @Inject constructor(
    private val recruiterRepository: RecruiterRepository
) : ViewModel() {
    private val _cvList = MutableStateFlow<List<CvResponse>>(emptyList())
    val cvList: StateFlow<List<CvResponse>> = _cvList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getCvList(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                recruiterRepository.getCv(id).fold(
                    onSuccess = { cvList ->
                        _cvList.value = cvList
                    },
                    onFailure = { exception ->
                        when {
                            exception.message?.contains("jobPost") == true -> {
                                _error.value = "Không tìm thấy thông tin công việc"
                            }
                            else -> {
                                _error.value = exception.message ?: "Không thể tải danh sách CV"
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                _error.value = "Đã xảy ra lỗi khi tải danh sách CV"
            } finally {
                _isLoading.value = false
            }
        }
    }
}