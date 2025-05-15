package com.example.findjob.data.remote.api

import com.example.findjob.data.model.common.RestResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AIApi {
    @Multipart
    @POST("ai/review-cv")
    suspend fun getAnalyzeCV(
        @Part file: MultipartBody.Part
    ): Response<RestResponse<String>>
}