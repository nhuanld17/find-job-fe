package com.example.findjob.data.model.request

data class UpdateEmployeeProfileRequest (
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: DateOfBirth,
    val gender: String,
    val location: String
)

data class DateOfBirth(
    val day: Int,
    val month: Int,
    val year: Int
)