package com.example.findjob.data.remote.api

import com.example.findjob.data.model.common.RestResponse
import com.example.findjob.data.model.request.ChangeAvatarRequest
import com.example.findjob.data.model.request.ChangePasswordRequest
import com.example.findjob.data.model.request.RecruiterRequest
import com.example.findjob.data.model.response.CvResponse
import com.example.findjob.data.model.response.RecruiterInfoResponse
import com.example.findjob.data.model.response.RecruiterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RecruiterApi {
    @POST("recruiter/update/profile")
    suspend fun updateRecruiterProfile(
        @Body recruiterRequest: RecruiterRequest
    ): Response<RestResponse<RecruiterResponse>>

    @GET("recruiter/get/profile")
    suspend fun getRecruiterProfile(): Response<RestResponse<RecruiterResponse>>

    @POST("employee/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest) :
            Response<RestResponse<Unit>>
    @POST("employee/change-avatar")
    suspend fun changeImageProfile(@Body request: ChangeAvatarRequest) : Response<RestResponse<String>>

    @GET("employee/avatar")
    suspend fun getProfileImage() : Response<RestResponse<String>>

    @GET("recruiter/get/info")
    suspend fun getRecruiterInfo(@Query("email") email: String): Response<RestResponse<RecruiterInfoResponse>>

    @GET("recruiter/get/cv")
    suspend fun getCv(@Query("id") id: Int): Response<RestResponse<List<CvResponse>>>
}
