package com.nemesis.jobsearch.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteVacanciesDao {

    @Query("SELECT vacancyId from FavoriteVacancyIdEntity")
    fun getFavoriteVacanciesIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FavoriteVacancyIdEntity)

    @Delete
    suspend fun delete(entity: FavoriteVacancyIdEntity)
}