package com.nemesis.jobsearch.data.vacancy

import kotlinx.serialization.Serializable

@Serializable
data class Vacancy(
    val id: String,
    val lookingNumber: Int = 0,
    val title: String,
    val address: Address,
    val company: String,
    val experience: Experience,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: Salary,
    val schedules: List<String>,
    val appliedNumber: Int = 0,
    val description: String? = null,
    val responsibilities: String,
    val questions: List<String>
)