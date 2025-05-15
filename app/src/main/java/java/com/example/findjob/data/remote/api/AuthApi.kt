package com.example.findjob.data.remote.api

import com.example.findjob.data.model.response.AuthResponse
import com.example.findjob.data.model.request.LoginRequest
import com.example.findjob.data.model.request.RegisterRequest
import com.example.findjob.data.model.common.RestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
// 2

/**
 * AuthApi là một interface định nghĩa các API liên quan đến xác thực (auth) như: Đăng nhập,
 * Đăng ký, Làm mới token. Nó được dùng cùng với Retrofit, một thư viện giúp gọi API.
 */
interface AuthApi {
    /**
     * @POST("auth/login"): Gửi HTTP POST đến đường dẫn /auth/login
     * @Body request: Dữ liệu gửi lên là một object kiểu LoginRequest
     * suspend fun: Hàm này là hàm tạm dừng (suspend) → dùng được trong coroutine
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<RestResponse<AuthResponse>>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RestResponse<AuthResponse>>
} 