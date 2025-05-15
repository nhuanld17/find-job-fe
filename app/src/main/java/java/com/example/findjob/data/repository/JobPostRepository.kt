package com.example.findjob.data.repository

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
import com.example.findjob.data.remote.api.JobPostApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobPostRepository @Inject constructor(
    private val jobPostApi: JobPostApi
) {
    suspend fun createJobPost(jobPostRequest: JobPostRequest): Result<Unit> {
        return try {
            val response = jobPostApi.createJobPost(jobPostRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to create job post"))
                }
            } else {
                Result.failure(Exception("Failed to create job post"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobPosts(): Result<List<ListJobResponse>> {
        return try {
            val response = jobPostApi.getJobPosts()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: emptyList())
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch job posts"))
                }
            } else {
                Result.failure(Exception("Failed to fetch job posts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobPostsRecent(): Result<List<ListJobResponse>> {
        return try {
            val response = jobPostApi.getJobPostsRecent()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: emptyList())
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch recent job posts"))
                }
            } else {
                Result.failure(Exception("Failed to fetch recent job posts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNewestJobsPosts() : Result<List<NewestJobResponse>> {
        return try {
            val response = jobPostApi.getNewestJobsPosts()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: emptyList())
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch newest job posts"))
                }
            } else {
                Result.failure(Exception("Failed to fetch newest job posts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveJob(request: SaveJobRequest): Result<SaveJobStatus> {
        return try {
            val response = jobPostApi.saveJob(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: SaveJobStatus(false))
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to save job"))
                }
            } else {
                Result.failure(Exception("Failed to save job"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSavedJobs(): Result<List<SavedJob>> {
        return try {
            val response = jobPostApi.getSavedJobs()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: emptyList())
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch saved jobs"))
                }
            } else {
                Result.failure(Exception("Failed to fetch saved jobs"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getJobPostDetail(id: Int): Result<JobDetailResponse> {
        return try {
            val response = jobPostApi.getJobPostDetail(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: JobDetailResponse("", "", "", "", "", "", "", "", "", "", "", ""))
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch job post detail"))
                }
            } else {
                Result.failure(Exception("Failed to fetch job post detail"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun applyJob(request: ApplyJobRequest): Result<Unit> {
        return try {
            val response = jobPostApi.applyJob(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to apply job"))
                }
            } else {
                Result.failure(Exception("Failed to apply job"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchWith(filter: Filter): Result<List<JobSearchResponse>> {
        return try {
            val response = jobPostApi.searchWith(filter)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    val jobs = responseBody?.data ?: emptyList()
                    println("Search results: $jobs")
                    Result.success(jobs)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to search jobs"))
                }
            } else {
                Result.failure(Exception("Failed to search jobs"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}