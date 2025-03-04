package com.nemesis.jobsearch.data.repository

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val RepositoryModule = module {
    singleOf(::JobSearchRepositoryImpl) {
        bind<JobSearchRepository>()
    }
}