package com.nemesis.jobsearch.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemesis.jobsearch.data.repository.JobSearchRepository
import com.nemesis.jobsearch.data.vacancy.Vacancy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritesViewModel(private val jobSearchRepository: JobSearchRepository) : ViewModel() {
    private val _state = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Loading)
    val state: StateFlow<FavoritesScreenState> = _state

    init {
        observeFavoriteVacancies()
    }

    private fun observeFavoriteVacancies() {
        viewModelScope.launch {
            jobSearchRepository
                .getFavoriteVacancies()
                .map { vacancies -> vacancies.filter(Vacancy::isFavorite) }
                .catch { e ->
                    _state.value = FavoritesScreenState.Error(errorText = e.message.orEmpty())
                }
                .collect { favoriteVacancies ->
                    _state.value = FavoritesScreenState.Ready(favoriteVacancies = favoriteVacancies)
                }
        }
    }

    fun removeVacancyFromFavorites(vacancy: Vacancy) {
        viewModelScope.launch {
            jobSearchRepository.setVacancyFavorite(vacancy, false)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _state.value = FavoritesScreenState.Loading
                jobSearchRepository.refresh()
                observeFavoriteVacancies()
            } catch (e: Throwable) {
                _state.value = FavoritesScreenState.Error(errorText = e.message.orEmpty())
            }
        }
    }

}