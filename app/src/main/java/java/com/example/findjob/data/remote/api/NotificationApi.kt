package com.example.findjob.data.remote.api

import com.example.findjob.data.model.response.NotificationResponse
import com.example.findjob.data.model.common.RestResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationApi {
    @GET("notification/get")
    suspend fun getNotification(): Response<RestResponse<List<NotificationResponse>>>

    @POST("notification/accept")
    suspend fun acceptNotification(@Query("id") id: Int): Response<RestResponse<String>>

    @POST("notification/reject")
    suspend fun rejectNotification(@Query("id") id: Int): Response<RestResponse<String>>

    @POST("notification/delete")
    suspend fun deleteNotification(@Query("id") id: Int): Response<RestResponse<String>>
}