package com.nemesis.jobsearch.ui.navigation

import kotlinx.serialization.Serializable

sealed class JobSearchRoute {

    @Serializable
    sealed class TopLevelRoute : JobSearchRoute() {
        @Serializable
        data object Search : TopLevelRoute()

        @Serializable
        data object Favorites : TopLevelRoute()

        @Serializable
        data object Feedback : TopLevelRoute()

        @Serializable
        data object Messages : TopLevelRoute()

        @Serializable
        data object Profile : TopLevelRoute()
    }

    @Serializable
    data class VacancyRoute(val vacancyId: String) : JobSearchRoute()
}

val TopLevelRoutes = listOf(
    JobSearchRoute.TopLevelRoute.Search,
    JobSearchRoute.TopLevelRoute.Favorites,
    JobSearchRoute.TopLevelRoute.Feedback,
    JobSearchRoute.TopLevelRoute.Messages,
    JobSearchRoute.TopLevelRoute.Profile
)