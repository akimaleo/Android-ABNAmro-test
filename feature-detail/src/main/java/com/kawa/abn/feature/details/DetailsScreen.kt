package com.kawa.abn.feature.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kawa.abn.foundation.compose.theme.components.ErrorState
import com.kawa.abn.foundation.compose.theme.theme.ABNAmroTheme

@Composable
fun DetailsScreen(
    id: Int,
    viewModel: DetailsScreenViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUrlClick: (String) -> Unit,
) {
    LaunchedEffect(id) {
        viewModel.loadData(id)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    DetailsScreenContent(
        state = state,
        onBackClick = onBackClick,
        onUrlClick = onUrlClick,
        onRetryClick = { viewModel.loadData(id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreenContent(
    state: DetailsScreenState,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
    onUrlClick: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        IconButton(onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "go back"
                            )
                        }

                        Image(
                            modifier = Modifier
                                .height(32.dp)
                                .align(Center),
                            painter = painterResource(com.kawa.abn.foundation.compose.R.drawable.abn_logo_large),
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Crossfade(modifier = Modifier.padding(innerPadding), targetState = state) { state ->
            when (state) {
                is DetailsScreenState.Error -> ErrorState(
                    message = state.message,
                    onClickRetry = onRetryClick,
                    modifier = Modifier.testTag("ErrorState")
                )

                DetailsScreenState.Loading -> CircularProgressIndicator(
                    Modifier
                        .size(100.dp)
                        .testTag("LoadingIndicator") // Test tag added
                )

                is DetailsScreenState.Success -> {
                    DetailsScreenSuccessContent(
                        repo = state.data,
                        onUrlClick = onUrlClick,
                    )
                }
            }
        }
    }
}


@Composable
internal fun DetailsScreenSuccessContent(
    repo: RepoDetailsViewData,
    onUrlClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            )
        } else {
            AsyncImage(
                model = repo.ownerImageUrl,
                contentDescription = "Owner Avatar",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            )
        }

        Text(
            text = repo.fullName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = repo.description ?: "No description available",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val iconPainter = if (repo.isPrivate) {
                painterResource(com.kawa.abn.foundation.compose.R.drawable.ic_lock_closed)
            } else {
                painterResource(com.kawa.abn.foundation.compose.R.drawable.ic_lock_open)
            }
            Icon(
                painter = iconPainter,
                contentDescription = null,
            )
            Text(text = repo.visibility)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onUrlClick(repo.htmlUrl) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Open in GitHub")
        }
    }
}


@PreviewLightDark
@Composable
internal fun PreviewRepoDetailsScreen() {
    val sampleRepo = RepoDetailsViewData(
        title = "Jetpack Compose",
        fullName = "android/compose",
        description = "Android’s modern toolkit for building native UI.",
        ownerImageUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
        visibility = "Public",
        isPrivate = false,
        htmlUrl = "https://github.com/android/compose"
    )
    ABNAmroTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            DetailsScreenContent(
                state = DetailsScreenState.Success(sampleRepo),
                onBackClick = {},
                onRetryClick = {},
                onUrlClick = {},
            )
        }
    }
}