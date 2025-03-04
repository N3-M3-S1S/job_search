package com.nemesis.jobsearch.data.api

import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.data.vacancy.Vacancy
import kotlinx.serialization.Serializable

@Serializable
internal data class JobSearchApiResponse(
    val offers: List<Offer>,
    val vacancies: List<Vacancy>
)
