package com.kawa.abn.foundation.compose.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kawa.abn.foundation.compose.theme.theme.ABNAmroTheme

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    message: String,
    onClickRetry: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable { onClickRetry() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "\uD83E\uDD37\u200D♂\uFE0F",
            fontSize = 75.sp,
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedButton(onClickRetry) {
            Text("Retry")
        }
    }
}

@PreviewLightDark()
@Composable()
private fun ErrorStatePreview() {
    ABNAmroTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            ErrorState(
                message = "Some error message",
                onClickRetry = {},
            )
        }
    }
}