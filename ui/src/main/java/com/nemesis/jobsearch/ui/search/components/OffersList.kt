package com.nemesis.jobsearch.ui.search.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.R
import com.nemesis.jobsearch.data.offer.Button
import com.nemesis.jobsearch.data.offer.Offer
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography

@Composable
fun OffersList(offers: List<Offer>, onOfferClick: (Offer) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
    ) {
        item {
            Spacer(modifier = Modifier.width(16.dp)) // todo handle start and end space at usage place
        }

        itemsIndexed(offers) { index, offer ->
            OfferListItem(offer = offer, onClick = onOfferClick)
            if (index != offers.lastIndex) {
                Spacer(modifier = Modifier.width(OffersPaddingBetweenItems))
            }
        }

        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun OfferListItem(offer: Offer, onClick: (Offer) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(OfferItemSize)
            .clip(OfferItemShape)
            .background(OfferItemColor)
            .clickable { onClick(offer) }
            .padding(OfferItemPadding)
    ) {
        val offerIconConfiguration = rememberOfferIconConfiguration(offer.id)
        if (offerIconConfiguration != null) {
            Box(
                modifier =
                Modifier
                    .padding(OfferItemIconPadding)
                    .size(OfferItemIconSize)
                    .clip(OfferItemIconShape)
                    .background(offerIconConfiguration.backgroundColor)
            ) {
                Icon(
                    painter = painterResource(offerIconConfiguration.iconResId),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        val hasButton = offer.button != null
        Column(modifier = Modifier.padding(OfferItemTextBlockPadding)) {
            Text(
                text = offer.title,
                style = OfferItemTitleStyle,
                color = OfferItemTitleColor,
                maxLines = if (hasButton) OfferItemTitleMaxLinesWithButtonText else OfferItemTitleMaxLinesWithoutButtonText
            )
            if (hasButton) {
                Text(
                    text = offer.button!!.text,
                    style = OfferItemTextButtonStyle,
                    color = OfferItemTextButtonColor,
                    maxLines = OfferItemTextButtonMaxLines
                )
            }
        }
    }
}

private data class OfferIconConfiguration(
    @DrawableRes val iconResId: Int,
    val backgroundColor: Color // TODO: add tint? (and for all other icons in the app)
)

@Composable
private fun rememberOfferIconConfiguration(offerId: String?): OfferIconConfiguration? =
    remember(offerId) {
        val iconResId: Int
        val backgroundColor: Color
        when (offerId) {
            OfferIdNearVacancies -> {
                iconResId =
                    R.drawable.ic_near_vacancies // this icon is missing in figma, using custom
                backgroundColor = JobSearchAppColors.DarkBlue
            }

            OfferIdLevelUp -> {
                iconResId = R.drawable.ic_level_up_resume
                backgroundColor = JobSearchAppColors.DarkGreen
            }

            OfferIdTemporary -> {
                iconResId = R.drawable.ic_temporary_job
                backgroundColor = JobSearchAppColors.DarkGreen
            }

            else -> return@remember null
        }
        OfferIconConfiguration(iconResId, backgroundColor)
    }

@Preview
@Composable
private fun OffersPreview() {
    JobSearchAppTheme {
        Surface {
            val offers = List(4) { index ->
                val id = when (index) {
                    0 -> OfferIdNearVacancies
                    1 -> OfferIdLevelUp
                    2 -> OfferIdTemporary
                    else -> null
                }
                Offer(
                    id = id,
                    title = "Title",
                    link = "",
                    button = if (index % 2 == 0) Button("Button") else null
                )
            }
            OffersList(
                offers = offers,
                onOfferClick = {},
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}


private val OffersPaddingBetweenItems = 8.dp

private val OfferItemSize = DpSize(width = 132.dp, height = 120.dp)
private val OfferItemShape = RoundedCornerShape(8.dp)
private val OfferItemColor = JobSearchAppColors.Gray1
private val OfferItemPadding = PaddingValues(start = 8.dp)

private val OfferItemIconSize = 32.dp
private val OfferItemIconShape = CircleShape
private val OfferItemIconPadding = PaddingValues(top = 10.dp)

private val OfferItemTextBlockPadding = PaddingValues(top = 58.dp, end = 12.dp, bottom = 11.dp)
private val OfferItemTitleStyle = JobSearchAppTypography.title4
private val OfferItemTitleColor = JobSearchAppColors.White
private const val OfferItemTitleMaxLinesWithButtonText = 2
private const val OfferItemTitleMaxLinesWithoutButtonText = 3
private val OfferItemTextButtonStyle = JobSearchAppTypography.text1
private val OfferItemTextButtonColor = JobSearchAppColors.Green
private const val OfferItemTextButtonMaxLines = 1

private const val OfferIdNearVacancies = "near_vacancies"
private const val OfferIdLevelUp = "level_up_resume"
private const val OfferIdTemporary = "temporary_job"
