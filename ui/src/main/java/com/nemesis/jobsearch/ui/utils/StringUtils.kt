package com.nemesis.jobsearch.ui.utils

import android.content.res.Configuration
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.pluralStringResource
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun rememberLocalizedQuantityString(
    @PluralsRes id: Int,
    quantity: Int,
    locale: Locale = Locale("ru", "RU")
): String {
    val isPreview = LocalInspectionMode.current
    if (!isPreview) {
        val context = LocalContext.current
        return remember(context, quantity, locale) {
            val config = Configuration(context.resources.configuration).apply { setLocale(locale) }
            context.createConfigurationContext(config).resources.getQuantityString(
                id,
                quantity,
                quantity
            )
        }
    } else { // cant use method above in preview because createConfigurationContext returns null
        return pluralStringResource(id, quantity, quantity)
    }
}

fun createLocalizedVacancyDayAndMonthString(
    date: String,
    locale: Locale = Locale("ru", "RU")
): Result<String> = runCatching {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = inputFormat.parse(date)!!
    val outputFormat = SimpleDateFormat("d MMMM", locale)
    outputFormat.format(parsedDate)
}
