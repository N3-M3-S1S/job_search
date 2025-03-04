package com.nemesis.jobsearch.ui.search

import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.data.vacancy.Vacancy

sealed class SearchScreenState {
    data object Loading : SearchScreenState()

    data class VacanciesPreview(
        val offers: List<Offer> = emptyList(),
        val previewVacancies: List<Vacancy> = emptyList(),
        val moreVacanciesCount: Int = 0
    ) : SearchScreenState()

    data class AllVacancies(val vacancies: List<Vacancy> = emptyList()) : SearchScreenState()

    data class Error(val errorText: String) : SearchScreenState()
}
