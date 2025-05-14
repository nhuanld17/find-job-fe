package com.example.findjob.data.remote.interceptor

import com.example.findjob.utils.InfoManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Bạn cứ hình dung đơn giản đây là "người gác cổng", mỗi lần app gọi API, class
 * này sẽ tự động gắn access token vào request nếu có.
 * AuthInterceptor là một Interceptor của OkHttp, dùng để: Tự động thêm access
 * token vào các request API trong phần Authorization header.
 */

/**
 * @Inject constructor(...): Dùng cho Hilt/Dagger để tự động cung cấp TokenManager.
 * tokenManager: Class bạn đã viết, dùng để lấy access token từ SharedPreferences.
 * : Interceptor: Class này triển khai interface Interceptor của OkHttp → dùng để
 * chặn và sửa request trước khi gửi đi.
 */
class AuthInterceptor @Inject constructor(
    private val infoManager: InfoManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Lấy Request gốc (request mà app định gửi đi).
        // Tạo bản sao có thể chỉnh sửa bằng newBuilder().
        val requestBuilder = chain.request().newBuilder()

        // Lấy access token từ TokenManager: nếu có token, thì tiếp tục
        // thêm token vào header: Authorization: Bearer <access_token>
        infoManager.getAccessToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        // Dùng chain.proceed(...) để gửi request đi.
        // build() tạo ra bản request cuối cùng với token đã gắn.
        return chain.proceed(requestBuilder.build())
    }
} 