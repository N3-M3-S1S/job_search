package com.nemesis.jobsearch.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.R
import com.nemesis.jobsearch.data.vacancy.Address
import com.nemesis.jobsearch.data.vacancy.Experience
import com.nemesis.jobsearch.data.vacancy.Salary
import com.nemesis.jobsearch.data.vacancy.Vacancy
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography
import com.nemesis.jobsearch.ui.utils.createLocalizedVacancyDayAndMonthString
import com.nemesis.jobsearch.ui.utils.rememberLocalizedQuantityString

@Composable
fun VacanciesList(
    vacancies: List<Vacancy>,
    onVacancyClick: (Vacancy) -> Unit,
    onFavoriteClick: (Vacancy) -> Unit,
    moreVacanciesButtonConfiguration: MoreVacanciesButtonConfiguration? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(SpaceBetweenVacancyListItems),
        modifier = modifier
    ) {
        items(items = vacancies, key = { it.id }) { vacancy ->
            VacancyListItem(
                vacancy = vacancy,
                onClick = onVacancyClick,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.animateItem()
            )
        }

        moreVacanciesButtonConfiguration?.let {
            item {
                MoreVacanciesButton(
                    vacanciesCount = it.vacanciesCount,
                    onClick = it.onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = SpaceBetweenVacancyLastListItemAndMoreVacanciesButton)
                )
            }
        }

    }
}

data class MoreVacanciesButtonConfiguration(val vacanciesCount: Int, val onClick: () -> Unit)

@Composable
private fun VacancyListItem(
    vacancy: Vacancy,
    onClick: (Vacancy) -> Unit,
    onFavoriteClick: (Vacancy) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clip(VacancyCardShape)
        .background(VacancyCardColor)
        .clickable { onClick(vacancy) }
        .padding(VacancyCardPadding)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            VacancyInfo(vacancy = vacancy, modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.width(SpaceBetweenVacancyInfoAndFavoriteIcon))
            FavoriteIcon(favorite = vacancy.isFavorite, onClick = { onFavoriteClick(vacancy) })
        }
        Spacer(Modifier.height(SpaceBetweenVacancyInfoAndApplyButton))
        FilledButton(
            text = stringResource(R.string.vacancy_apply),
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun VacancyInfo(vacancy: Vacancy, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SpaceBetweenVacancyInfoItems),
        modifier = modifier
    ) {
        if (vacancy.lookingNumber > 0) {
            LookingNumberText(vacancy.lookingNumber)
        }
        VacancyTitle(vacancy.title)
        vacancy.salary.short?.let { shortSalary ->
            ShortSalaryText(shortSalary)
        }
        Column {
            TownText(vacancy.address.town)
            Spacer(modifier = Modifier.height(SpaceBetweenTownTextAndCompanyInfo))
            CompanyInfo(company = vacancy.company)
        }
        ExperiencePreview(experiencePreview = vacancy.experience.previewText)
        PublishedDate(vacancy.publishedDate)
    }
}

@Composable
private fun LookingNumberText(lookingNumber: Int, modifier: Modifier = Modifier) {
    val localizedResources =
        rememberLocalizedQuantityString(R.plurals.looking_for_vacancy, lookingNumber)
    Text(
        text = localizedResources,
        style = LookingNumberTextStyle,
        color = LookingNumberTextColor,
        modifier = modifier
    )
}

@Composable
private fun VacancyTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = VacancyTitleTextStyle,
        color = VacancyTitleTextColor,
        modifier = modifier
    )
}

@Composable
private fun ShortSalaryText(shortSalary: String, modifier: Modifier = Modifier) {
    Text(
        text = shortSalary,
        style = ShortSalaryTextStyle,
        color = ShortSalaryTextColor,
        modifier = modifier
    )
}

@Composable
private fun TownText(town: String, modifier: Modifier = Modifier) {
    Text(
        text = town,
        style = TownTextStyle,
        color = TownTextColor,
        modifier = modifier
    )
}

@Composable
private fun CompanyInfo(company: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(
            text = company,
            style = CompanyTextStyle,
            color = CompanyTextColor,
        )
        Spacer(modifier = Modifier.width(CompanySpaceBetweenTextAndIcon))
        Icon(
            painter = painterResource(R.drawable.ic_company_approved),
            contentDescription = null,
            tint = CompanyApprovedIconTint
        )
    }
}

@Composable
private fun ExperiencePreview(experiencePreview: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_experience),
            contentDescription = null,
            tint = ExperienceIconTint
        )
        Spacer(modifier = Modifier.width(SpaceBetweenExperienceIconAndText))
        Text(
            text = experiencePreview,
            style = ExperiencePreviewTextStyle,
            color = ExperiencePreviewTextColor,
        )
    }
}

@Composable
private fun PublishedDate(date: String, modifier: Modifier = Modifier) {
    val dayAndMonthText = remember(date) { // TODO: extract to utils if used somewhere else
        createLocalizedVacancyDayAndMonthString(date).getOrElse {
            Log.e("JobSearch", "Date parse error ${it.message}")
            ""
        }
    }
    if (dayAndMonthText.isNotEmpty()) {
        Text(
            text = stringResource(R.string.published_date_format, dayAndMonthText),
            style = PublishedDateTextStyle,
            color = PublishedDateTextColor,
            modifier = modifier
        )
    }
}

@Composable
private fun FavoriteIcon(favorite: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val iconResId = if (favorite) FavoriteActiveIconResId else FavoriteInactiveIconResId
    Icon( // TODO: custom click animation
        painter = painterResource(iconResId),
        contentDescription = null,
        tint = FavoriteIconTint,
        modifier = modifier.clickable(
            onClick = onClick,
            interactionSource = null,
            indication = null
        )
    )
}

@Composable
private fun MoreVacanciesButton(
    vacanciesCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localizedMoreVacanciesString =
        rememberLocalizedQuantityString(R.plurals.vacancies_count, vacanciesCount)
    val moreVacanciesString = stringResource(R.string.more_vacancies, localizedMoreVacanciesString)
    ContainedButton(text = moreVacanciesString, onClick = onClick, modifier = modifier)
}

@Preview(locale = "ru")
@Composable
private fun VacancyListItemPreview() {
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
                )
            }
        }
    }

    JobSearchAppTheme {
        Surface {
            VacanciesList(
                vacancies = testVacancies,
                onVacancyClick = {},
                onFavoriteClick = {
                    val updatedVacancy = it.copy(isFavorite = !it.isFavorite)
                    testVacancies.replaceAll { vacancy -> if (vacancy.id == updatedVacancy.id) updatedVacancy else vacancy }
                },
                moreVacanciesButtonConfiguration = MoreVacanciesButtonConfiguration(
                    vacanciesCount = 10,
                    onClick = {}),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

private val SpaceBetweenVacancyListItems = 16.dp

private val SpaceBetweenVacancyLastListItemAndMoreVacanciesButton = 8.dp

private val VacancyCardShape = RoundedCornerShape(8.dp)
private val VacancyCardColor = JobSearchAppColors.Gray1
private val VacancyCardPadding = PaddingValues(16.dp)

private val SpaceBetweenVacancyInfoItems = 10.dp

private val LookingNumberTextStyle = JobSearchAppTypography.text1
private val LookingNumberTextColor = JobSearchAppColors.Green

private val VacancyTitleTextStyle = JobSearchAppTypography.title3
private val VacancyTitleTextColor = JobSearchAppColors.White

private val ShortSalaryTextStyle = JobSearchAppTypography.title1
private val ShortSalaryTextColor = JobSearchAppColors.White

private val TownTextStyle = JobSearchAppTypography.text1
private val TownTextColor = JobSearchAppColors.White

private val SpaceBetweenTownTextAndCompanyInfo = 4.dp

private val CompanyTextStyle = JobSearchAppTypography.text1
private val CompanyTextColor = JobSearchAppColors.White
private val CompanyApprovedIconTint = JobSearchAppColors.Gray3
private val CompanySpaceBetweenTextAndIcon = 8.dp

private val ExperienceIconTint = JobSearchAppColors.White
private val ExperiencePreviewTextStyle = JobSearchAppTypography.text1
private val ExperiencePreviewTextColor = JobSearchAppColors.White
private val SpaceBetweenExperienceIconAndText = 8.dp

private val PublishedDateTextStyle = JobSearchAppTypography.text1
private val PublishedDateTextColor = JobSearchAppColors.Gray3

private val SpaceBetweenVacancyInfoAndApplyButton =
    21.dp // this space is inconsistent in figma, don't know if intentional, use fixed value for now

private val FavoriteInactiveIconResId = R.drawable.ic_favorites
private val FavoriteActiveIconResId = R.drawable.ic_favorites_active
private val FavoriteIconTint = JobSearchAppColors.Blue

private val SpaceBetweenVacancyInfoAndFavoriteIcon =
    16.dp // didn't find this value in figma so i use my own

