package com.nemesis.jobsearch.ui.favorites

import com.nemesis.jobsearch.data.vacancy.Vacancy

data class FavoritesScreenActions(
    val onVacancyClick: (Vacancy) -> Unit,
    val onVacancyFavoriteClick: (Vacancy) -> Unit,
    val onRetryClick: () -> Unit
)