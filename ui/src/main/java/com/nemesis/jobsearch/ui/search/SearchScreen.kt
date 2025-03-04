package com.nemesis.jobsearch.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.R
import com.nemesis.jobsearch.data.offer.Button
import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.data.vacancy.Address
import com.nemesis.jobsearch.data.vacancy.Experience
import com.nemesis.jobsearch.data.vacancy.Salary
import com.nemesis.jobsearch.data.vacancy.Vacancy
import com.nemesis.jobsearch.ui.components.ErrorScreen
import com.nemesis.jobsearch.ui.components.ErrorScreenButtonConfiguration
import com.nemesis.jobsearch.ui.components.MoreVacanciesButtonConfiguration
import com.nemesis.jobsearch.ui.components.VacanciesList
import com.nemesis.jobsearch.ui.search.components.OffersList
import com.nemesis.jobsearch.ui.search.components.VacanciesSearchBar
import com.nemesis.jobsearch.ui.search.components.VacanciesSearchBarConfiguration
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography
import com.nemesis.jobsearch.ui.utils.rememberLocalizedQuantityString
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    navigateToVacancyScreen: (Vacancy) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current
    val actions = remember(navigateToVacancyScreen, viewModel, uriHandler) {
        SearchScreenActions(
            onReturnToVacanciesPreviewClick = viewModel::showPreviewVacancies,
            onOfferClick = { offer -> uriHandler.openUri(offer.link) },
            onVacancyClick = navigateToVacancyScreen,
            onFavoriteClick = viewModel::toggleVacancyFavorite,
            onMoreVacanciesClick = viewModel::showAllVacancies,
            onRetryClick = viewModel::refresh
        )
    }

    SearchScreen(state = state, actions = actions)
}

@Composable
private fun SearchScreen(state: SearchScreenState, actions: SearchScreenActions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(SearchScreenPadding)
    ) {
        when (state) {
            is SearchScreenState.Loading -> LoadingIndicator()
            is SearchScreenState.VacanciesPreview -> VacanciesPreviewContent(state, actions)
            is SearchScreenState.AllVacancies -> AllVacanciesContent(state, actions)
            is SearchScreenState.Error -> ErrorScreen(
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
private fun VacanciesPreviewContent(
    state: SearchScreenState.VacanciesPreview,
    actions: SearchScreenActions
) {
    val searchBarConfig = rememberSearchBarConfigurationForVacanciesPreviewState()
    SearchContent(
        searchBarConfig = searchBarConfig,
        actions = actions,
        headerContent = {
            if (state.offers.isNotEmpty()) {
                val layoutDirection = LocalLayoutDirection.current
                OffersList(
                    offers = state.offers,
                    onOfferClick = actions.onOfferClick,
                    modifier = Modifier
                        .layout { measurable, constraints -> // layout out of bounds
                            val placeable = measurable.measure(
                                constraints.copy(
                                    maxWidth = constraints.maxWidth + SearchScreenPadding.calculateLeftPadding(
                                        layoutDirection
                                    ).roundToPx() + SearchScreenPadding.calculateRightPadding(
                                        layoutDirection
                                    ).roundToPx()
                                )
                            )
                            layout(placeable.width, placeable.height) {
                                placeable.place(0, 0)
                            }
                        }
                )
                VerticalSpacer(SpaceBelowOffersList)
            }
            VacanciesForYouTitle()
            VerticalSpacer(SpaceBelowVacanciesForYouTitle)
        },
        vacanciesList = state.previewVacancies,
        moreVacanciesConfig = if (state.moreVacanciesCount > 0) MoreVacanciesButtonConfiguration(
            vacanciesCount = state.moreVacanciesCount,
            onClick = actions.onMoreVacanciesClick
        ) else null
    )
}

@Composable
private fun AllVacanciesContent(
    state: SearchScreenState.AllVacancies,
    actions: SearchScreenActions
) {
    val searchBarConfig = rememberSearchBarConfigurationForAllVacanciesState(
        searchBarIconAction = actions.onReturnToVacanciesPreviewClick
    )
    SearchContent(
        searchBarConfig = searchBarConfig,
        actions = actions,
        headerContent = {
            VacanciesSortBar(vacanciesCount = state.vacancies.size)
            VerticalSpacer(SpaceBelowVacanciesSortBar)
        },
        vacanciesList = state.vacancies,
        moreVacanciesConfig = null
    )
}

@Composable
private fun SearchContent(
    searchBarConfig: VacanciesSearchBarConfiguration,
    actions: SearchScreenActions,
    headerContent: @Composable () -> Unit,
    vacanciesList: List<Vacancy>,
    moreVacanciesConfig: MoreVacanciesButtonConfiguration?
) {
    TopBar(searchBarConfiguration = searchBarConfig)
    VerticalSpacer(SpaceBelowTopBar)

    headerContent()

    VacanciesList(
        vacancies = vacanciesList,
        onVacancyClick = actions.onVacancyClick,
        onFavoriteClick = actions.onFavoriteClick,
        moreVacanciesButtonConfiguration = moreVacanciesConfig,
        modifier = Modifier.fillMaxSize()
    )
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
private fun TopBar(
    searchBarConfiguration: VacanciesSearchBarConfiguration,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        VacanciesSearchBar(
            searchBarConfiguration = searchBarConfiguration,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(TopBarSpaceBetweenItems))
        FilterButton()
    }
}

@Composable
private fun FilterButton(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(FilterButtonSize)
            .clip(FilterButtonShape)
            .background(color = FilterButtonColor)
            .clickable { }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_filter),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun VacanciesSortBar(vacanciesCount: Int, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        VacanciesCountText(vacanciesCount = vacanciesCount)
        VacanciesSortButton()
    }
}

@Composable
private fun VacanciesCountText(vacanciesCount: Int, modifier: Modifier = Modifier) {
    val vacanciesCountString =
        rememberLocalizedQuantityString(R.plurals.vacancies_count, vacanciesCount)
    Text(
        text = vacanciesCountString,
        style = VacanciesCountTextStyle,
        color = VacanciesCountTextColor,
        modifier = modifier
    )
}

@Composable
private fun VacanciesSortButton(modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.vacancies_sort_text),
            style = VacanciesSortButtonStyle,
            color = VacanciesSortButtonColor,
            modifier = modifier
        )
        Spacer(modifier = Modifier.width(VacanciesSortButtonSpaceBetweenTextAndIcon))
        Icon(
            painter = painterResource(R.drawable.ic_sort),
            contentDescription = null,
            tint = VacanciesSortButtonColor
        )
    }
}

@Composable
private fun VacanciesForYouTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.vacancies_for_you_title),
        style = VacanciesForYouTextStyle,
        color = VacanciesForYouTextColor,
        modifier = modifier
    )
}

@Composable
private fun rememberSearchBarConfigurationForVacanciesPreviewState(): VacanciesSearchBarConfiguration =
    remember {
        VacanciesSearchBarConfiguration(
            searchBarHintResId = R.string.search_bar_vacancies_preview_hint,
            searchBarIconResId = R.drawable.ic_search,
            searchBarIconTint = JobSearchAppColors.Gray4,
        )
    }

@Composable
private fun rememberSearchBarConfigurationForAllVacanciesState(searchBarIconAction: () -> Unit): VacanciesSearchBarConfiguration =
    remember {
        VacanciesSearchBarConfiguration(
            searchBarHintResId = R.string.search_bar_all_vacancies_hint,
            searchBarIconResId = R.drawable.ic_left_arrow,
            searchBarIconTint = JobSearchAppColors.White,
            searchBarIconAction = searchBarIconAction
        )
    }

@Composable
private fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}


@Preview(
    device = "spec:width=1280px,height=2856px,dpi=480,navigation=buttons",
    locale = "ru",
    showSystemUi = true,
)
@Composable
private fun SearchScreenPreview() {
    JobSearchAppTheme {
        Surface {
            val testOffers = List(5) {
                Offer(
                    id = it.toString(),
                    title = "Offer $it",
                    link = "",
                    button = if (it % 2 == 0) Button("Button") else null
                )
            }

            val testVacancies = remember {
                List(5) {
                    Vacancy(
                        id = "$it",
                        lookingNumber = 2,
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
                        isFavorite = false,
                        salary = Salary(
                            short = "1500-2900 Br"
                        ),
                        schedules = emptyList(),
                        appliedNumber = 0,
                        description = "",
                        responsibilities = "",
                        questions = emptyList()
                    )
                }.toMutableStateList()
            }

            val previewVacanciesSize = 3

            val previewVacancies =
                remember { testVacancies.take(previewVacanciesSize).toMutableStateList() }


            val vacanciesPreviewState = SearchScreenState.VacanciesPreview(
                offers = testOffers,
                previewVacancies = previewVacancies,
                moreVacanciesCount = testVacancies.count() - previewVacancies.count()
            )

            val allVacanciesState = SearchScreenState.AllVacancies(vacancies = testVacancies)


            var state: SearchScreenState by remember { mutableStateOf(vacanciesPreviewState) }

            SearchScreen(
                state = state,
                actions = SearchScreenActions(
                    onReturnToVacanciesPreviewClick = { state = vacanciesPreviewState },
                    onOfferClick = {},
                    onVacancyClick = {},
                    onFavoriteClick = {
                        val updatedVacancy = it.copy(isFavorite = !it.isFavorite)
                        previewVacancies.replaceAll { vacancy -> if (vacancy.id == updatedVacancy.id) updatedVacancy else vacancy }
                        testVacancies.replaceAll { vacancy -> if (vacancy.id == updatedVacancy.id) updatedVacancy else vacancy }
                    },
                    onMoreVacanciesClick = {
                        state = allVacanciesState
                    },
                    onRetryClick = {})
            )
        }
    }
}

private val SearchScreenPadding =
    PaddingValues(
        start = 16.dp,
        top = 16.dp,
        end = 16.dp,
        bottom = 8.dp
    )
private val LoadingIndicatorColor = JobSearchAppColors.Blue

private val TopBarSpaceBetweenItems = 8.dp
private val SpaceBelowTopBar = 16.dp

private val FilterButtonSize = 40.dp
private val FilterButtonShape = RoundedCornerShape(10.dp)
private val FilterButtonColor = JobSearchAppColors.Gray2

private val VacanciesCountTextStyle = JobSearchAppTypography.text1
private val VacanciesCountTextColor = JobSearchAppColors.White

private val VacanciesSortButtonStyle = JobSearchAppTypography.text1
private val VacanciesSortButtonColor = JobSearchAppColors.Blue
private val VacanciesSortButtonSpaceBetweenTextAndIcon = 6.dp
private val SpaceBelowVacanciesSortBar = 25.dp


private val VacanciesForYouTextStyle = JobSearchAppTypography.title1
private val VacanciesForYouTextColor = JobSearchAppColors.White
private val SpaceBelowVacanciesForYouTitle = 16.dp

private val SpaceBelowOffersList = 32.dp
