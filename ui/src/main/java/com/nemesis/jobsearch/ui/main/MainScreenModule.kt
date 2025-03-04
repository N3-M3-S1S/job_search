package com.nemesis.jobsearch.ui.main

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val MainModule = module {
    viewModelOf(::MainScreenViewModel)
}