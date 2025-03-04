package com.nemesis.jobsearch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nemesis.jobsearch.ui.theme.JobSearchAppColors
import com.nemesis.jobsearch.ui.theme.JobSearchAppTheme
import com.nemesis.jobsearch.ui.theme.JobSearchAppTypography

@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = FilledButtonDefaultColors
) {
    Button(
        onClick = onClick,
        shape = FilledButtonShape,
        enabled = enabled,
        colors = colors,
        modifier = modifier.height(FilledButtonHeight)
    ) {
        Text(text = text, style = JobSearchAppTypography.filledButtonText, maxLines = 1)
    }
}

@Preview
@Composable
private fun FilledButtonPreview() {
    JobSearchAppTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)

            ) {
                repeat(2) {
                    FilledButton(
                        text = "Text",
                        enabled = it == 0,
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


val FilledButtonDefaultColors = ButtonColors(
    contentColor = JobSearchAppColors.White,
    containerColor = JobSearchAppColors.Green,
    disabledContainerColor = JobSearchAppColors.DarkGreen,
    disabledContentColor = JobSearchAppColors.Gray4
)
private val FilledButtonShape = RoundedCornerShape(50.dp)
private val FilledButtonHeight = 32.dp