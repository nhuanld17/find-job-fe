package com.example.findjob.data.model.response

import com.example.findjob.data.model.common.JobIntroDTO

data class RecruiterInfoResponse (
    val imageLink: String,
    val recruiterName: String,
    val location: String,
    val about: String,
    val website: String,
    val industry: String,
    val since: String,
    val jobIntroDTOs: List<JobIntroDTO>,
)