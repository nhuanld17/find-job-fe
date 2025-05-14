package com.example.findjob.data.repository

import com.example.findjob.data.model.common.RestResponse
import com.example.findjob.data.model.request.LoginRequest
import com.example.findjob.data.model.request.RegisterRequest
import com.example.findjob.data.model.response.AuthResponse
import com.example.findjob.data.remote.api.AuthApi
import com.example.findjob.utils.InfoManager
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * - AuthRepository là lớp trung gian nằm giữa UI (ViewModel) và API
 * - Vai trò chính của AuthRepository:
 * + Gửi yêu cầu đăng nhập/đăng ký tới API
 * + Xử lý response từ server
 * + Nếu thành công → lưu accessToken bằng TokenManager
 * + Trả Result<AuthResponse> về ViewModel để xử lý tiếp (hiển thị UI, điều hướng, v.v.)
 */

/**
 * @Inject constructor(...): Cho phép Hilt tự tạo AuthRepository, inject các tham số api
 * và tokenManager.
 * @Singleton: Chỉ tạo 1 lần duy nhất trong suốt vòng đời app
 * api: Giao tiếp với server qua Retrofit
 * tokenManager: Lưu accessToken, thường dùng SharedPreferences hoặc DataStore
 */
@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val infoManager: InfoManager
) {
    private val gson = Gson()

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            if (errorBody == null) return "Unknown error"
            val errorResponse = gson.fromJson(errorBody, RestResponse::class.java)
            errorResponse.message ?: "Unknown error"
        } catch (e: Exception) {
            "Unknown error"
        }
    }

    /**
     * - Tạo 1 object LoginRequest chứa username và password
     * - Gửi đến api.login(...)
     * - Nếu thành công → truyền response vào handleLoginResponse(...)
     * - Nếu lỗi mạng hoặc crash → return Result.failure(...)
     * - suspend → là function bất đồng bộ (dùng trong Coroutine)
     */
    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(username, password))
            handleLoginResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(role: String, name: String, email: String, password: String): Result<Unit> {
        return try {
            val response = api.register(RegisterRequest(role, name, email, password))
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Registration failed"))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //  Xử lý response cho login, lưu token sau khi login thành công
    private fun handleLoginResponse(response: Response<RestResponse<AuthResponse>>): Result<AuthResponse> {
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody == null) {
                Result.failure(Exception("Invalid response format"))
            } else if (responseBody.statusCode in 200..299) {
                val authResponse = responseBody.data
                if (authResponse != null) {
                    infoManager.saveInfo(authResponse = authResponse)
                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("Invalid response format"))
                }
            } else {
                Result.failure(Exception(responseBody.message ?: "Authentication failed"))
            }
        } else {
            val errorMessage = parseErrorMessage(response.errorBody()?.string())
            Result.failure(Exception(errorMessage))
        }
    }
}