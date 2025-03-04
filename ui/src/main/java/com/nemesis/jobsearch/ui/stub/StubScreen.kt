package com.nemesis.jobsearch.ui.stub

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StubScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Stub", modifier = Modifier.align(Alignment.Center))
    }
}
