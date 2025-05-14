package com.example.findjob.data.model.common

import java.util.Date

data class JobIntroDTO (
    val id: Int,
    val title: String,
    val salary: String,
    val position: String,
    val jobType: String,
    val createAt: Date,
    val isSaved: Boolean
)