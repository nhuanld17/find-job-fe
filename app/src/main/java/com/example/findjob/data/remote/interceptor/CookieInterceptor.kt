package com.example.findjob.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor để thêm access token vào header của request
 */
class CookieInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(newRequest)
    }
} 