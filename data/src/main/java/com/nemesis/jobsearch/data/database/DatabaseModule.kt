package com.nemesis.jobsearch.data.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val DatabaseModule = module {
    single { Room.databaseBuilder(androidContext(), JobSearchDatabase::class.java, "db").build() }
    single { get<JobSearchDatabase>().favoriteVacanciesDao() }
}