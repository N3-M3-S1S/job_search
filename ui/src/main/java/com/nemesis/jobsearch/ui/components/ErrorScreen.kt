package com.nemesis.jobsearch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography

@Composable
fun ErrorScreen(
    title: String,
    errorText: String? = null,
    buttonConfiguration: ErrorScreenButtonConfiguration? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(ErrorScreenPadding),
        verticalArrangement = Arrangement.spacedBy(
            ErrorScreenSpaceBetweenItems,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorTitle(title)
        if (errorText != null) {
            ErrorText(errorText)
        }
        if (buttonConfiguration != null) {
            ErrorButton(buttonConfiguration)
        }
    }
}

@Composable
private fun ErrorTitle(text: String, modifier: Modifier = Modifier) {
    Text(text = text, style = ErrorTitleStyle, color = ErrorTitleColor, textAlign = TextAlign.Center, modifier = modifier)
}

@Composable
private fun ErrorText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = ErrorTextStyle,
        color = ErrorTextColor,
        textAlign = TextAlign.Center,
        maxLines = ErrorTextMaxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
private fun ErrorButton(
    configuration: ErrorScreenButtonConfiguration,
    modifier: Modifier = Modifier
) {
    ContainedButton(text = configuration.text, onClick = configuration.onClick, modifier = modifier)
}

private val ErrorScreenPadding = PaddingValues(16.dp)
private val ErrorScreenSpaceBetweenItems = 16.dp

private val ErrorTitleStyle = JobSearchAppTypography.title1
private val ErrorTitleColor = JobSearchAppColors.White

private val ErrorTextStyle = JobSearchAppTypography.title3
private val ErrorTextColor = JobSearchAppColors.White
private const val ErrorTextMaxLines = 3

@Preview
@Composable
private fun ErrorScreenPreview() {
    JobSearchAppTheme {
        ErrorScreen(
            title = "Title",
            errorText = "error text ".repeat(10),
            buttonConfiguration = ErrorScreenButtonConfiguration(text = "Button", onClick = {})
        )
    }
}

data class ErrorScreenButtonConfiguration(val text: String, val onClick: () -> Unit)