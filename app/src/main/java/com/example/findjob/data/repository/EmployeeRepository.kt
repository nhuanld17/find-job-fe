package com.example.findjob.data.repository

import com.example.findjob.data.model.request.ChangeAvatarRequest
import com.example.findjob.data.model.request.ChangePasswordRequest
import com.example.findjob.data.model.response.EmployeeProfileDTO
import com.example.findjob.data.model.response.UpdateEmployeeProfileResponse
import com.example.findjob.data.remote.api.EmployeeApi
import com.example.findjob.utils.InfoManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRepository @Inject constructor(
    private val employeeApi: EmployeeApi,
    private val infoManager: InfoManager
){
    suspend fun getEmployeeProfile(): Result<EmployeeProfileDTO> {
        return try {
            val response = employeeApi.getEmployeeProfile()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data!!)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to get profile"))
                }
            } else {
                Result.failure(Exception("Failed to get profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEmployeeProfile(employeeProfileDTO: EmployeeProfileDTO) : Result<UpdateEmployeeProfileResponse> {
        return try {
            val response = employeeApi.updateEmployeeProfile(employeeProfileDTO)
            // Nếu update thành công thì update thông tin lưu trong sharedpref
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    val updateResponse = responseBody?.data
                    infoManager.updateInfo(updateResponse)
                    Result.success(responseBody?.data!!)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to update profile"))
                }
            } else {
                Result.failure(Exception("Failed to update profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(passwordRequest: ChangePasswordRequest) : Result<Unit> {
        return try {
            val response = employeeApi.changePassword(passwordRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    // Nếu status code trong khoảng 200-299 thì coi như thành công
                    Result.success(Unit)
                } else {
                    // Nếu status code không nằm trong khoảng 200-299 thì trả về lỗi với message từ server
                    Result.failure(Exception(responseBody?.message ?: "Failed to change password"))
                }
            } else {
                // Nếu response không thành công (isSuccessful = false) thì trả về lỗi
                Result.failure(Exception("Failed to change password: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changeImageProfile(imageUrl: String): Result<String> {
        return try {
            val request = ChangeAvatarRequest(imageUrl)
            val response = employeeApi.changeImageProfile(request)
            
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: imageUrl)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to change avatar"))
                }
            } else {
                Result.failure(Exception("Failed to change avatar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfileImage(): Result<String> {
        return try {
            val response = employeeApi.getProfileImage()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: "")
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to get profile image"))
                }
            } else {
                Result.failure(Exception("Failed to get profile image"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}