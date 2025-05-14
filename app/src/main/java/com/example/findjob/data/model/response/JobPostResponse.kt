package com.example.findjob.data.model.response

data class ListJobResponse (
    val id: Int,
    val title: String?,
    val imageUrl: String?,
    val description: String?,
    val position: String?,
    val qualification: String?,
    val experience: String?,
    val type: String?,
    val salary: String?,
    val expirateAt: String?,
    val createdAt: String?,
    val nameCompany: String?,
    val location: String?,
    val avatar: String?
)