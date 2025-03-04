package com.nemesis.jobsearch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val jobSearchAppColorScheme = darkColorScheme(
    surface = JobSearchAppColors.Black,
    surfaceContainer = JobSearchAppColors.Gray1
)

@Composable
fun JobSearchAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = jobSearchAppColorScheme,
        content = content
    )
}