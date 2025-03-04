package com.nemesis.jobsearch.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemesis.jobsearch.data.repository.JobSearchRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

class MainScreenViewModel(private val jobSearchRepository: JobSearchRepository) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    init {
        observeFavoritesCount()
    }

    private fun observeFavoritesCount() {
        viewModelScope.launch {
            jobSearchRepository.getFavoriteVacancies().map { it.size }
                .retry { // bad solution, didn't have time for proper one
                    delay(1000)
                    true
                }
                .collect {
                    _state.value = MainScreenState(favoritesCount = it)
                }
        }
    }
}