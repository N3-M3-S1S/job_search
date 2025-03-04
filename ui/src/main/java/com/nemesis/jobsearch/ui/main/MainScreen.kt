package com.nemesis.jobsearch.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nemesis.jobsearch.data.vacancy.Vacancy
import com.nemesis.jobsearch.ui.favorites.FavoritesScreen
import com.nemesis.jobsearch.ui.navigation.JobSearchNavigationBar
import com.nemesis.jobsearch.ui.navigation.JobSearchRoute
import com.nemesis.jobsearch.ui.navigation.TopLevelRoutes
import com.nemesis.jobsearch.ui.search.SearchScreen
import com.nemesis.jobsearch.ui.stub.StubScreen
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    MainScreen(state = state)
}

@Composable
private fun MainScreen(state: MainScreenState) {
    JobSearchAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                val navController = rememberNavController()
                var selectedTopLevelRoute: JobSearchRoute.TopLevelRoute by remember {
                    mutableStateOf(
                        JobSearchRoute.TopLevelRoute.Search
                    )
                }

                LaunchedEffect(Unit) {
                    navController.currentBackStackEntryFlow.collect {
                        val destination = it.destination
                        TopLevelRoutes.forEach { route ->
                            if (destination.hasRoute(route::class)) {
                                selectedTopLevelRoute = route
                                return@forEach
                            }
                        }
                    }
                }

                val navigateToVacancyScreen = remember {
                    { vacancy: Vacancy ->
                        navController.navigate(
                            JobSearchRoute.VacancyRoute(vacancy.id)
                        )
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = JobSearchRoute.TopLevelRoute.Search,
                    modifier = Modifier.weight(1f)
                ) {
                    composable<JobSearchRoute.TopLevelRoute.Search> {
                        SearchScreen(navigateToVacancyScreen = navigateToVacancyScreen)
                    }
                    composable<JobSearchRoute.TopLevelRoute.Favorites> {
                        FavoritesScreen(navigateToVacancyScreen = navigateToVacancyScreen)
                    }
                    composable<JobSearchRoute.TopLevelRoute.Feedback> {
                        StubScreen()
                    }
                    composable<JobSearchRoute.TopLevelRoute.Messages> {
                        StubScreen()
                    }
                    composable<JobSearchRoute.TopLevelRoute.Profile> {
                        StubScreen()
                    }
                    composable<JobSearchRoute.VacancyRoute> {
                        StubScreen()
                    }
                }

                JobSearchNavigationBar(
                    currentRoute = selectedTopLevelRoute,
                    onRouteSelected = { selectedRoute ->
                        if (selectedRoute != selectedTopLevelRoute) {
                            selectedTopLevelRoute = selectedRoute
                            navController.navigate(selectedRoute)
                        }
                    },
                    favoritesCount = state.favoritesCount,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                )
            }
        }
    }
}
