package com.example.findjob.data.repository

import com.example.findjob.data.model.request.ChangeAvatarRequest
import com.example.findjob.data.model.request.ChangePasswordRequest
import com.example.findjob.data.model.request.RecruiterRequest
import com.example.findjob.data.model.response.CvResponse
import com.example.findjob.data.model.response.RecruiterInfoResponse
import com.example.findjob.data.model.response.RecruiterResponse
import com.example.findjob.data.remote.api.RecruiterApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.ranges.contains

@Singleton
class RecruiterRepository @Inject constructor(
    private val recruiterApi: RecruiterApi
) {
    suspend fun updateRecruiterProfile(
        recruiterRequest: RecruiterRequest
    ): Result<Unit> {
        return try {
            val response = recruiterApi.updateRecruiterProfile(recruiterRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to update recruiter profile"))
                }
            } else {
                Result.failure(Exception("Failed to update recruiter profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getRecruiterProfile(): Result<RecruiterResponse> {
        return try {
            val response = recruiterApi.getRecruiterProfile()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: RecruiterResponse(
                        about = null,
                        website = null,
                        industry = null,
                        location = null,
                        since = null,
                        specialization = null
                    ))
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch recruiter profile"))
                }
            } else {
                Result.failure(Exception("Failed to fetch recruiter profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun changePassword(passwordRequest: ChangePasswordRequest) : Result<Unit> {
        return try {
            val response = recruiterApi.changePassword(passwordRequest)
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
            val response = recruiterApi.changeImageProfile(request)

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
            val response = recruiterApi.getProfileImage()
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

    suspend fun getRecruiterInfo(email: String): Result<RecruiterInfoResponse> {
        return try {
            val response = recruiterApi.getRecruiterInfo(email)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: RecruiterInfoResponse(
                        imageLink = "",
                        recruiterName = "",
                        location = "",
                        about = "",
                        website = "",
                        industry = "",
                        since = "",
                        jobIntroDTOs = emptyList()
                    ))
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch recruiter info"))
                }
            } else {
                Result.failure(Exception("Failed to fetch recruiter info"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCv(id: Int): Result<List<CvResponse>> {
        return try {
            val response = recruiterApi.getCv(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: emptyList())
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch CV"))
                }
            } else {
                Result.failure(Exception("Failed to fetch CV"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}