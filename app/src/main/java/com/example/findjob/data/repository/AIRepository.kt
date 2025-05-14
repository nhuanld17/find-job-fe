package com.example.findjob.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.findjob.data.model.common.RestResponse
import com.example.findjob.data.model.response.CVAnalysisResponse
import com.example.findjob.data.remote.api.AIApi
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepository @Inject constructor(
    private val api: AIApi,
    @ApplicationContext private val context: Context
) {
    private val TAG = "AIRepository"

    suspend fun analyzeCV(uri: Uri): Result<CVAnalysisResponse> {
        return try {
            // Tạo file tạm thời từ Uri
            val tempFile = File.createTempFile("cv_", ".pdf", context.cacheDir)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Tạo request body từ file
            val requestFile = tempFile.asRequestBody("application/pdf".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
            
            val response = api.getAnalyzeCV(body)
            Log.d(TAG, "Response code: ${response.code()}")
            Log.d(TAG, "Response body: ${response.body()}")
            Log.d(TAG, "Response error body: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.statusCode in 200..299) {
                    val analysis = responseBody?.data ?: ""
                    Log.d(TAG, "Analysis content: $analysis")
                    Result.success(CVAnalysisResponse(analysis = analysis))
                } else {
                    Log.e(TAG, "Error status code: ${responseBody?.statusCode}")
                    Result.failure(Exception(responseBody?.message ?: "Failed to analyze CV"))
                }
            } else {
                Log.e(TAG, "Request failed with code: ${response.code()}")
                Result.failure(Exception("Failed to analyze CV: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception occurred", e)
            Result.failure(e)
        }
    }
} 