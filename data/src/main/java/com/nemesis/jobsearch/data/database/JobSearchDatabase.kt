package com.nemesis.jobsearch.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [FavoriteVacancyIdEntity::class])
internal abstract class JobSearchDatabase : RoomDatabase() {

    abstract fun favoriteVacanciesDao(): FavoriteVacanciesDao
}