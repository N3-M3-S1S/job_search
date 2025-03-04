package com.nemesis.jobsearch.data.vacancy

import kotlinx.serialization.Serializable

@Serializable
data class Experience(
    val previewText: String,
    val text: String
)