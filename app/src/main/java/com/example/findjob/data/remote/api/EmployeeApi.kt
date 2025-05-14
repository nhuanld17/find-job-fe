package com.example.findjob.data.remote.api

import com.example.findjob.data.model.common.RestResponse
import com.example.findjob.data.model.request.ChangeAvatarRequest
import com.example.findjob.data.model.request.ChangePasswordRequest
import com.example.findjob.data.model.response.EmployeeProfileDTO
import com.example.findjob.data.model.response.UpdateEmployeeProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EmployeeApi {
    @GET("employee/profile")
    suspend fun getEmployeeProfile() : Response<RestResponse<EmployeeProfileDTO>>

    @GET("employee/avatar")
    suspend fun getProfileImage() : Response<RestResponse<String>>

    @POST("employee/change-avatar")
    suspend fun changeImageProfile(@Body request: ChangeAvatarRequest) : Response<RestResponse<String>>

    @POST("employee/profile")
    suspend fun updateEmployeeProfile(@Body employeeProfileDTO: EmployeeProfileDTO) :
            Response<RestResponse<UpdateEmployeeProfileResponse>>

    @POST("employee/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest) :
            Response<RestResponse<Unit>>
}