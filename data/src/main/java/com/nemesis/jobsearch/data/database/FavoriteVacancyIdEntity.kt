package com.nemesis.jobsearch.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class FavoriteVacancyIdEntity(@PrimaryKey val vacancyId: String)