package com.nemesis.jobsearch.data.repository

import com.nemesis.jobsearch.data.api.JobSearchApi
import com.nemesis.jobsearch.data.database.FavoriteVacanciesDao
import com.nemesis.jobsearch.data.database.FavoriteVacancyIdEntity
import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.data.vacancy.Vacancy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class JobSearchRepositoryImpl(
    private val api: JobSearchApi,
    private val favoriteVacanciesDao: FavoriteVacanciesDao
) : JobSearchRepository {
    private var needRefresh = true
    private val mutex = Mutex()

    private val offers = MutableSharedFlow<List<Offer>>(replay = 1)
    private val vacancies = MutableSharedFlow<List<Vacancy>>(replay = 1)

    override fun getOffers(): Flow<List<Offer>> = offers.onStart { refreshIfNeeded() }

    /*
        The implementation is inefficient, using it for simplicity only.
        It also overwrites the 'isFavorite' value from the repository's response for consistency. Without overwriting it, a vacancy will become favorite after a refresh, even if a user marked it as not favorite before.
     */
    override fun getVacancies(): Flow<List<Vacancy>> = vacancies
        .combine(favoriteVacanciesDao.getFavoriteVacanciesIds()) { vacancies, favoriteVacanciesIds ->
            vacancies.map { vacancy: Vacancy ->
                vacancy.copy(isFavorite = favoriteVacanciesIds.contains(vacancy.id))
            }
        }.onStart { refreshIfNeeded() }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> =
        getVacancies().map { it.filter(Vacancy::isFavorite) }

    override suspend fun setVacancyFavorite(vacancy: Vacancy, favorite: Boolean) {
        mutex.withLock {
            if (needRefresh) {
                return
            }
            val favoriteVacancyIdEntity = FavoriteVacancyIdEntity(vacancy.id)
            if (favorite) {
                favoriteVacanciesDao.insert(favoriteVacancyIdEntity)
            } else {
                favoriteVacanciesDao.delete(favoriteVacancyIdEntity)
            }
        }
    }

    override suspend fun refresh() {
        mutex.withLock {
            needRefresh = true
            refreshIfNeeded(lockMutex = false)
        }
    }

    private suspend fun refreshIfNeeded(lockMutex: Boolean = true) {
        if (lockMutex) {
            mutex.lock()
        }
        try {
            if (needRefresh) {
                val apiResponse = api.query()
                offers.emit(apiResponse.offers)
                vacancies.emit(apiResponse.vacancies)
                needRefresh = false
            }
        } finally {
            if (lockMutex) {
                mutex.unlock()
            }
        }
    }

}