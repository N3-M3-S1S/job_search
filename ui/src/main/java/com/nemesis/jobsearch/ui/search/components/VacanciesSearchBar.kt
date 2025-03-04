package com.nemesis.jobsearch.ui.search.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.R
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography

@Composable
fun VacanciesSearchBar(
    searchBarConfiguration: VacanciesSearchBarConfiguration,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier
            .clip(SearchBarShape)
            .background(SearchBalColor)
            .height(SearchBarHeight)
            .padding(SearchBarPadding)
    ) {
        Icon(
            painter = painterResource(searchBarConfiguration.searchBarIconResId),
            contentDescription = null,
            tint = searchBarConfiguration.searchBarIconTint,
            modifier = Modifier.clickable(enabled = searchBarConfiguration.searchBarIconAction != null) { searchBarConfiguration.searchBarIconAction?.invoke() }
        )
        Spacer(modifier = Modifier.width(SearchBarSpaceBetweenItems))
        Text( // TODO: make text field 
            text = stringResource(searchBarConfiguration.searchBarHintResId),
            style = SearchBarTextStyle,
            maxLines = SearchBarTextMaxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class VacanciesSearchBarConfiguration(
    @StringRes val searchBarHintResId: Int, // todo change to string?
    @DrawableRes val searchBarIconResId: Int,
    val searchBarIconTint: Color,
    val searchBarIconAction: (() -> Unit)? = null
)

@Preview
@Composable
private fun SearchBarPreview(){
    JobSearchAppTheme {
        Surface {
            val searchBarConfiguration = VacanciesSearchBarConfiguration(
                searchBarHintResId = R.string.search_bar_vacancies_preview_hint,
                searchBarIconResId = R.drawable.ic_search,
                searchBarIconTint = JobSearchAppColors.Gray4,
            )
            VacanciesSearchBar(searchBarConfiguration = searchBarConfiguration, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
        }
    }
}

private val SearchBarTextStyle = JobSearchAppTypography.text1.copy(color = JobSearchAppColors.Gray4)
private val SearchBarShape = RoundedCornerShape(8.dp)
private val SearchBalColor = JobSearchAppColors.Gray2
private val SearchBarHeight = 40.dp
private val SearchBarPadding = PaddingValues(horizontal = 8.dp)
private val SearchBarSpaceBetweenItems = 8.dp
private const val SearchBarTextMaxLines = 1

