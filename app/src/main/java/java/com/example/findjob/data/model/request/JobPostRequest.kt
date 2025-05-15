package com.example.findjob.data.model.request

import com.google.gson.annotations.SerializedName
import java.util.Date

data class JobPostRequest(
    val title: String,
    val description: String,
    val requirement: String,
    val position: String,
    val qualification: String,
    val experience: String,
    val type: String,
    @SerializedName("workplaceType")
    val workplaceType: String,
    val salary: String,
    @SerializedName("expirateAt")
    val expirateAt: Date
)


