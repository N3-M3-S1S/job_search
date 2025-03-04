package com.nemesis.jobsearch.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.R
import com.nemesis.jobsearch.data.vacancy.Address
import com.nemesis.jobsearch.data.vacancy.Experience
import com.nemesis.jobsearch.data.vacancy.Salary
import com.nemesis.jobsearch.data.vacancy.Vacancy
import com.nemesis.jobsearch.ui.components.ErrorScreen
import com.nemesis.jobsearch.ui.components.ErrorScreenButtonConfiguration
import com.nemesis.jobsearch.ui.components.VacanciesList
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography
import com.nemesis.jobsearch.ui.utils.rememberLocalizedQuantityString
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    navigateToVacancyScreen: (Vacancy) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val actions = remember(navigateToVacancyScreen, viewModel) {
        FavoritesScreenActions(
            onVacancyClick = navigateToVacancyScreen,
            onVacancyFavoriteClick = viewModel::removeVacancyFromFavorites,
            onRetryClick = viewModel::refresh
        )
    }
    FavoritesScreen(state = state, actions = actions)
}

@Composable
private fun FavoritesScreen(state: FavoritesScreenState, actions: FavoritesScreenActions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(FavoritesScreenPadding)
    ) {
        when (state) {
            FavoritesScreenState.Loading -> LoadingIndicator()

            is FavoritesScreenState.Ready -> {
                FavoritesTitle()
                Spacer(modifier = Modifier.height(SpaceBelowFavoritesTitle))
                FavoritesCountText(favoritesCount = state.favoriteVacancies.size)
                Spacer(modifier = Modifier.height(SpaceBelowFavoritesCountText))
                VacanciesList(
                    vacancies = state.favoriteVacancies,
                    onVacancyClick = actions.onVacancyClick,
                    onFavoriteClick = actions.onVacancyFavoriteClick,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is FavoritesScreenState.Error -> ErrorScreen(
                title = stringResource(R.string.error_title),
                errorText = state.errorText,
                buttonConfiguration = ErrorScreenButtonConfiguration(
                    text = stringResource(R.string.retry),
                    onClick = actions.onRetryClick
                )
            )
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = LoadingIndicatorColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun FavoritesTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.favorites_title),
        style = FavoritesTitleStyle,
        color = FavoritesTitleColor,
        modifier = modifier
    )
}

@Composable
private fun FavoritesCountText(favoritesCount: Int, modifier: Modifier = Modifier) {
    val favoritesCountText =
        rememberLocalizedQuantityString(R.plurals.vacancies_count, favoritesCount)
    Text(
        text = favoritesCountText,
        style = FavoritesCountTextStyle,
        color = FavoritesCountTextColor,
        modifier = modifier
    )
}

@Preview
@Composable
private fun FavoritesScreenPreview() {
    JobSearchAppTheme {
        Surface {
            val testVacancies = remember {
                mutableStateListOf<Vacancy>().apply {
                    repeat(5) {
                        add(
                            Vacancy(
                                id = "$it",
                                lookingNumber = it,
                                title = "Дизайнер для маркетплейсов Wildberries, Ozon",
                                address = Address(
                                    town = "Минск",
                                    street = "",
                                    house = ""
                                ),
                                company = "Еком дизайн",
                                experience = Experience(
                                    previewText = "Опыт от 1 года до 3 лет",
                                    text = ""
                                ),
                                publishedDate = "2026-02-16",
                                isFavorite = true,
                                salary = Salary(
                                    short = "1500-2900 Br"
                                ),
                                schedules = emptyList(),
                                appliedNumber = 0,
                                description = "",
                                responsibilities = "",
                                questions = emptyList()
                            )
                        )
                    }
                }
            }


            val state = FavoritesScreenState.Ready(favoriteVacancies = testVacancies)
            val actions = FavoritesScreenActions(
                onVacancyClick = {},
                onVacancyFavoriteClick = { testVacancies.remove(it) },
                onRetryClick = {})
            FavoritesScreen(state = state, actions = actions)
        }
    }
}

private val FavoritesScreenPadding =
    PaddingValues(
        start = 16.dp,
        top = 32.dp,
        end = 16.dp,
        bottom = 8.dp
    )

private val LoadingIndicatorColor = JobSearchAppColors.Blue

private val FavoritesTitleStyle = JobSearchAppTypography.title1
private val FavoritesTitleColor = JobSearchAppColors.White
private val SpaceBelowFavoritesTitle = 24.dp

private val FavoritesCountTextStyle = JobSearchAppTypography.text1
private val FavoritesCountTextColor = JobSearchAppColors.Gray3
private val SpaceBelowFavoritesCountText = 16.dp
