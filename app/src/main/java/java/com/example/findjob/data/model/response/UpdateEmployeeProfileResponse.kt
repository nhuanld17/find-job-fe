package com.example.findjob.data.model.response

data class UpdateEmployeeProfileResponse(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: DateOfBirth,
    val gender: String,
    val location: String,
    val token: String
)