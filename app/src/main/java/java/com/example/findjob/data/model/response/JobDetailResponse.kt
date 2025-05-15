package com.example.findjob.data.model.response

data class JobDetailResponse(
    val imageLink: String,
    val recruiterName: String,
    val recruiterMail: String,
    val jobTitle: String,
    val location: String,
    val description: String,
    val requirement: String,
    val position: String,
    val qualification: String,
    val experience: String,
    val jobType: String,
    val salary: String
)
