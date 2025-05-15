package com.example.findjob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findjob.data.model.response.RecruiterInfoResponse
import com.example.findjob.data.repository.RecruiterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val recruiterRepository: RecruiterRepository
) : ViewModel() {
    private val _companyInfo = MutableStateFlow<RecruiterInfoResponse?>(null)
    val companyInfo: StateFlow<RecruiterInfoResponse?> = _companyInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getCompanyInfo(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            recruiterRepository.getRecruiterInfo(email)
                .onSuccess { response ->
                    _companyInfo.value = response
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load company info"
                }
            
            _isLoading.value = false
        }
    }
}