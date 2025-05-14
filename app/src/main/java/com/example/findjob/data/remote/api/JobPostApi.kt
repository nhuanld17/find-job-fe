package com.example.findjob.data.remote.api

import com.example.findjob.data.model.common.RestResponse
import com.example.findjob.data.model.request.ApplyJobRequest
import com.example.findjob.data.model.request.Filter
import com.example.findjob.data.model.request.JobPostRequest
import com.example.findjob.data.model.request.SaveJobRequest
import com.example.findjob.data.model.response.JobDetailResponse
import com.example.findjob.data.model.response.JobSearchResponse
import com.example.findjob.data.model.response.ListJobResponse
import com.example.findjob.data.model.response.NewestJobResponse
import com.example.findjob.data.model.response.SaveJobStatus
import com.example.findjob.data.model.response.SavedJob
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface JobPostApi {
    @POST("jobpost/create")
    suspend fun createJobPost(@Body jobPostRequest: JobPostRequest) : Response<RestResponse<JobPostRequest>>

    @GET("jobpost/list-recent")
    suspend fun getJobPostsRecent() : Response<RestResponse<List<ListJobResponse>>>

    @GET("jobpost/list")
    suspend fun getJobPosts() : Response<RestResponse<List<ListJobResponse>>>

    @GET("jobpost/newest-jobs")
    suspend fun getNewestJobsPosts() : Response<RestResponse<List<NewestJobResponse>>>

    @POST("jobpost/save-job")
    suspend fun saveJob(@Body request: SaveJobRequest) : Response<RestResponse<SaveJobStatus>>

    @GET("jobpost/saved-jobs")
    suspend fun getSavedJobs() : Response<RestResponse<List<SavedJob>>>

    @GET("jobpost/detail")
    suspend fun getJobPostDetail(@Query("id") id: Int) : Response<RestResponse<JobDetailResponse>>

    @POST("jobpost/apply")
    suspend fun applyJob(@Body request: ApplyJobRequest) : Response<RestResponse<Unit>>

    @POST("jobpost/search")
    suspend fun searchWith(@Body filter: Filter) : Response<RestResponse<List<JobSearchResponse>>>
}