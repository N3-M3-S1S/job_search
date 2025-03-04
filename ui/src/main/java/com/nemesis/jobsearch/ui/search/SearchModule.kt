package com.nemesis.jobsearch.ui.search

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val SearchModule = module {
    viewModelOf(::SearchViewModel)
}