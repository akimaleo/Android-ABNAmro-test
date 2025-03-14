package com.kawa.abn.featurelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kawa.abn.foundation.compose.theme.ABNAmroTheme

sealed class ReposListScreenState {
    data object Loading : ReposListScreenState()
    data class Content(val repos: List<String>) : ReposListScreenState()
    data class Error(val message: String) : ReposListScreenState()
}

@Composable
fun ReposListScreen(navigateToDetailsScreen: (id: String) -> Unit) {
    ReposListScreenContent(
        state = ReposListScreenState.Content(repos = listOf("one ", "two", "tree")),
        navigateToDetailsScreen = navigateToDetailsScreen,
    )
}

@Composable
private fun ReposListScreenContent(
    state: ReposListScreenState,
    navigateToDetailsScreen: (id: String) -> Unit
) {
    when (state) {
        is ReposListScreenState.Content -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.repos) { repo ->
                ListItem(repo)
            }
        }

        is ReposListScreenState.Error -> Text(
            text = state.message,
            color = MaterialTheme.colorScheme.error,
        )

        ReposListScreenState.Loading -> CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }

}

@Composable
private fun ListItem(value: String) {
    Row(modifier = Modifier) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)) {
            Text(
                text = value.substring(0..2).uppercase(),
                color = MaterialTheme.colorScheme.primaryContainer,
            )
        }
    }
}

@Preview
@Composable
private fun ReposListScreenContentSuccessPreview() {
    ABNAmroTheme {
        ReposListScreenContent(
            state = ReposListScreenState.Content(repos = listOf("one ", "two", "tree")),
            navigateToDetailsScreen = {}
        )
    }
}
