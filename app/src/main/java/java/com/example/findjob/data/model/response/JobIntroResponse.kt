package com.example.findjob.data.model.response

data class JobIntroResponse (
    val id: String,
    val title: String,
    val companyName: String,
    val location: String,
    val salary: String,
    val description: String,
    val requirements: List<String>,
    val benefits: List<String>,
    val postedDate: String,
    val applyLink: String
)