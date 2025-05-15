package com.example.findjob.data.model.request

data class ChangePasswordRequest (
    private val current : String,
    private val newPassword : String,
    private val confirmPassword : String
)