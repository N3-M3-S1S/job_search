package com.nemesis.jobsearch.data.vacancy

import kotlinx.serialization.Serializable

@Serializable
data class Salary(
    val full: String? = null,
    val short: String? = null
)