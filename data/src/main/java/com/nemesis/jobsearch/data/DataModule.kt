package com.nemesis.jobsearch.data

import com.nemesis.jobsearch.data.api.ApiModule
import com.nemesis.jobsearch.data.database.DatabaseModule
import com.nemesis.jobsearch.data.repository.RepositoryModule
import org.koin.dsl.module

val DataModule = module {
    includes(ApiModule, RepositoryModule, DatabaseModule)
}