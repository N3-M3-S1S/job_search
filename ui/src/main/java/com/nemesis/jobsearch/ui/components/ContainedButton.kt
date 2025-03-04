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
fun ContainedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ContainedButtonDefaultColors
) {
    Button(
        onClick = onClick,
        shape = ContainedButtonShape,
        enabled = enabled,
        colors = colors,
        modifier = modifier.height(ContainedButtonHeight)
    ) {
        Text(text = text, style = JobSearchAppTypography.containedButtonText, maxLines = 1)
    }
}

@Preview(device = "spec:parent=pixel_5")
@Composable
private fun BigButtonPreview() {
    JobSearchAppTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)

            ) {
                repeat(2) {
                    ContainedButton(
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

val ContainedButtonDefaultColors = ButtonColors(
    contentColor = JobSearchAppColors.White,
    containerColor = JobSearchAppColors.Blue,
    disabledContainerColor = JobSearchAppColors.DarkBlue,
    disabledContentColor = JobSearchAppColors.Gray4
)
private val ContainedButtonShape = RoundedCornerShape(8.dp)
private val ContainedButtonHeight = 40.dp