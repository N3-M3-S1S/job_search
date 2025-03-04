package com.nemesis.jobsearch.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.R
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography

@Composable
fun JobSearchNavigationBar(
    currentRoute: JobSearchRoute.TopLevelRoute,
    onRouteSelected: (JobSearchRoute.TopLevelRoute) -> Unit,
    favoritesCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = NavBarBorderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = NavBarBorderWidth.toPx()
                )
            }
            .padding(NavBarPadding)
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TopLevelDestinations.forEach { jobSearchDestination ->
            val badgeCount =
                if (jobSearchDestination.route == JobSearchRoute.TopLevelRoute.Favorites) favoritesCount else 0
            JobSearchNavigationBarItem(
                textResId = jobSearchDestination.titleResId,
                iconResId = jobSearchDestination.iconResId,
                selected = currentRoute == jobSearchDestination.route,
                onClick = {
                    onRouteSelected(jobSearchDestination.route)
                },
                badgeCount = badgeCount
            )
        }
    }
}

@Composable
private fun RowScope.JobSearchNavigationBarItem(
    @StringRes textResId: Int,
    @DrawableRes iconResId: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0
) {
    val interactionSource = remember { MutableInteractionSource() }
    val color by animateColorAsState(if (selected) NavItemActiveColor else NavItemInactiveColor)
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) NavItemPressedScale else 1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .weight(1f)
            .scale(scale)
            .padding(bottom = 8.dp)
    ) {
        Box {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(NavItemIconSize)
            )
            if (badgeCount > 0) {
                val badgeText =
                    if (badgeCount <= NavItemBadgeMaxValue) badgeCount.toString() else NavItemBadgeOverflowValue
                var fontSize by remember { mutableStateOf(JobSearchAppTypography.badgeText.fontSize) }
                Box(
                    Modifier
                        .size(NavItemBadgeSize)
                        .align(Alignment.TopEnd)
                        .offset(x = NavItemBadgeHorizontalOffset)
                        .clip(NavItemBadgeShape)
                        .background(NavItemBadgeColor)
                ) {
                    Text(
                        badgeText,
                        style = JobSearchAppTypography.badgeText,
                        fontSize = fontSize,
                        onTextLayout = {
                            if (it.hasVisualOverflow) {
                                fontSize *= 0.9f
                            }
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        Text(
            text = stringResource(textResId),
            style = JobSearchAppTypography.tabText,
            color = color,
            maxLines = 1,
            modifier = Modifier.padding(NavItemTextPadding)
        )
    }
}

private data class JobSearchDestination(
    val route: JobSearchRoute.TopLevelRoute,
    val titleResId: Int,
    val iconResId: Int
)

private val TopLevelDestinations = listOf(
    JobSearchDestination(
        route = JobSearchRoute.TopLevelRoute.Search,
        titleResId = R.string.tab_search,
        iconResId = R.drawable.ic_search
    ),
    JobSearchDestination(
        route = JobSearchRoute.TopLevelRoute.Favorites,
        titleResId = R.string.tab_favorite,
        iconResId = R.drawable.ic_favorites
    ),
    JobSearchDestination(
        route = JobSearchRoute.TopLevelRoute.Feedback,
        titleResId = R.string.tab_feedback,
        iconResId = R.drawable.ic_feedback
    ),
    JobSearchDestination(
        route = JobSearchRoute.TopLevelRoute.Messages,
        titleResId = R.string.tab_messages,
        iconResId = R.drawable.ic_messages
    ),
    JobSearchDestination(
        route = JobSearchRoute.TopLevelRoute.Profile,
        titleResId = R.string.tab_profile,
        iconResId = R.drawable.ic_profile
    ),
)

@Preview
@Composable
private fun JobSearchNavigationBarPreview() {
    var currentRoute: JobSearchRoute.TopLevelRoute by remember { mutableStateOf(JobSearchRoute.TopLevelRoute.Search) }
    val favoritesCount = 10
    JobSearchAppTheme {
        Surface {
            JobSearchNavigationBar(
                currentRoute = currentRoute,
                onRouteSelected = { currentRoute = it },
                favoritesCount = favoritesCount,
            )
        }
    }
}

private val NavBarBorderColor = JobSearchAppColors.Gray1
private val NavBarBorderWidth = 1.dp
private val NavBarPadding = PaddingValues(bottom = 8.dp, top = 6.dp)
private val NavItemActiveColor = JobSearchAppColors.Blue
private val NavItemInactiveColor = JobSearchAppColors.Gray4
private val NavItemIconSize = 24.dp
private val NavItemBadgeSize = 13.dp
private const val NavItemPressedScale = 0.97f
private val NavItemBadgeShape = RoundedCornerShape(16.dp)
private val NavItemBadgeColor = JobSearchAppColors.Red
private val NavItemBadgeHorizontalOffset = 4.dp
private const val NavItemBadgeMaxValue = 99
private const val NavItemBadgeOverflowValue = "$NavItemBadgeMaxValue+"
private val NavItemTextPadding = PaddingValues(top = 3.dp)