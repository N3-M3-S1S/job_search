package com.nemesis.jobsearch.ui.search

import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.data.vacancy.Vacancy

data class SearchScreenActions(
    val onReturnToVacanciesPreviewClick: () -> Unit,
    val onOfferClick: (Offer) -> Unit,
    val onVacancyClick: (Vacancy) -> Unit,
    val onFavoriteClick: (Vacancy) -> Unit,
    val onMoreVacanciesClick: () -> Unit,
    val onRetryClick: () -> Unit
)