package com.example.findjob.data.repository

import com.example.findjob.data.model.response.NotificationResponse
import com.example.findjob.data.remote.api.NotificationApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationApi: NotificationApi
){
    suspend fun getNotification(): Result<List<NotificationResponse>> {
        return try {
            val response = notificationApi.getNotification()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(responseBody?.data ?: emptyList())
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to fetch notifications"))
                }
            } else {
                Result.failure(Exception("Failed to fetch notifications"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptNotification(id: Int): Result<Unit> {
        return try {
            val response = notificationApi.acceptNotification(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to accept notification"))
                }
            } else {
                Result.failure(Exception("Failed to accept notification"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectNotification(id: Int): Result<Unit> {
        return try {
            val response = notificationApi.rejectNotification(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to reject notification"))
                }
            } else {
                Result.failure(Exception("Failed to reject notification"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun deleteNotification(id: Int): Result<Unit> {
        return try {
            val response = notificationApi.deleteNotification(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(responseBody?.message ?: "Failed to delete notification"))
                }
            } else {
                Result.failure(Exception("Failed to delete notification"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}