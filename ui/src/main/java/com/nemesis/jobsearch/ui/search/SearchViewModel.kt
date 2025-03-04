package com.nemesis.jobsearch.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.data.repository.JobSearchRepository
import com.nemesis.jobsearch.data.vacancy.Vacancy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(private val jobSearchRepository: JobSearchRepository) : ViewModel() {

    private val _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val state: StateFlow<SearchScreenState> = _state

    private var offers = emptyList<Offer>()

    private var vacancies = emptyList<Vacancy>()

    private val vacanciesCountToDisplayInPreviewState = 3

    init {
        viewModelScope.launch {
            observeVacanciesAndOffers()
        }
    }

    private suspend fun observeVacanciesAndOffers() {
        jobSearchRepository
            .getVacancies()
            .combine(jobSearchRepository.getOffers()) { vacancies, offers ->
                vacancies to offers
            }
            .catch { e ->
                _state.value = SearchScreenState.Error(errorText = e.message.orEmpty())
            }
            .collect { (newVacancies, newOffers) ->
                vacancies = newVacancies
                offers = newOffers

                _state.update { currentState ->
                    when (currentState) {
                        is SearchScreenState.VacanciesPreview -> {
                            currentState.copy(
                                previewVacancies = createPreviewVacanciesList(),
                                moreVacanciesCount = getMoreVacanciesCount()
                            )
                        }

                        is SearchScreenState.AllVacancies -> {
                            currentState.copy(vacancies = newVacancies)
                        }

                        else -> {
                            SearchScreenState.VacanciesPreview(
                                offers = offers,
                                previewVacancies = createPreviewVacanciesList(),
                                moreVacanciesCount = getMoreVacanciesCount()
                            )
                        }
                    }
                }
            }
        Log.d("Catched", "Finished")
    }

    fun toggleVacancyFavorite(vacancy: Vacancy) {
        viewModelScope.launch {
            jobSearchRepository.setVacancyFavorite(
                vacancy = vacancy,
                favorite = !vacancy.isFavorite
            )
        }
    }

    fun showAllVacancies() {
        _state.value = SearchScreenState.AllVacancies(vacancies = vacancies)
    }

    fun showPreviewVacancies() {
        _state.value = SearchScreenState.VacanciesPreview(
            offers = offers,
            previewVacancies = createPreviewVacanciesList(),
            moreVacanciesCount = getMoreVacanciesCount()
        )
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _state.value = SearchScreenState.Loading
                jobSearchRepository.refresh()
                observeVacanciesAndOffers()
            } catch (e: Throwable) {
                _state.value = SearchScreenState.Error(e.message.orEmpty())
            }
        }
    }

    private fun createPreviewVacanciesList() = vacancies.take(vacanciesCountToDisplayInPreviewState)

    private fun getMoreVacanciesCount() =
        (vacancies.size - vacanciesCountToDisplayInPreviewState).coerceAtLeast(0)

}