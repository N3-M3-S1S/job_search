package com.nemesis.jobsearch.ui

import com.nemesis.jobsearch.ui.favorites.FavoritesModule
import com.nemesis.jobsearch.ui.main.MainModule
import com.nemesis.jobsearch.ui.search.SearchModule
import org.koin.dsl.module

val UiModule = module {
    includes(MainModule, SearchModule, FavoritesModule)
}