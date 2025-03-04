package com.nemesis.jobsearch.ui.favorites

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val FavoritesModule = module {
    viewModelOf(::FavoritesViewModel)
}