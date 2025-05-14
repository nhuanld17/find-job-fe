package com.example.findjob.data.model.response

import java.util.Date

data class JobSearchResponse(
    val id: Int,
    val imageUrl: String,
    val jobTitle: String,
    val companyName: String,
    val location: String,
    val jobPosition: String,
    val jobType: String,
    val salary: String,
    val createdAt: Date,
    val saved: Boolean
)
