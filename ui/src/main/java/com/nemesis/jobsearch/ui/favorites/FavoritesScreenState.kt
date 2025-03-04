package com.nemesis.jobsearch.ui.favorites

import com.nemesis.jobsearch.data.vacancy.Vacancy

sealed class FavoritesScreenState {
    data object Loading : FavoritesScreenState()

    data class Ready(val favoriteVacancies: List<Vacancy>) : FavoritesScreenState()

    data class Error(val errorText: String) : FavoritesScreenState()
}