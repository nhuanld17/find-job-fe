package com.example.findjob.data.model.common

data class RestResponse<T>(
    val statusCode: Int,
    val message: String,
    val error: String?,
    val data: T?,
    val path: String,
    val timestamp: String
)