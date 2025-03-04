package com.nemesis.jobsearch.data.repository

import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.data.vacancy.Vacancy
import kotlinx.coroutines.flow.Flow

interface JobSearchRepository {
    fun getOffers(): Flow<List<Offer>>

    fun getVacancies(): Flow<List<Vacancy>>

    fun getFavoriteVacancies(): Flow<List<Vacancy>>

    suspend fun setVacancyFavorite(vacancy: Vacancy, favorite: Boolean)

    suspend fun refresh()
}