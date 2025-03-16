package com.kawa.abn.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.kawa.abn.feature.list.domain.entity.Visibility
import com.kawa.abn.foundation.compose.theme.components.ErrorState
import com.kawa.abn.foundation.compose.theme.components.loading.PageLoaderAnimation
import com.kawa.abn.foundation.compose.theme.theme.ABNAmroTheme
import timber.log.Timber

@Composable
fun ReposListScreen(
    viewModel: ReposListViewModel = hiltViewModel(),
    navigateToDetailsScreen: (id: Int) -> Unit
) {
    val state = viewModel.listDataFlow.collectAsLazyPagingItems()
    ReposListContent(
        state = state,
        navigateToDetailsScreen = navigateToDetailsScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReposListContent(
    state: LazyPagingItems<RepoViewData>,
    navigateToDetailsScreen: (id: Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth()) {
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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            Timber.d("${state.loadState}")
            items(
                count = state.itemCount,
                key = state.itemKey { it.id }
            ) { index ->
                val item = state[index]
                item?.let {
                    Timber.d("data[$index] = $item")
                    ListItem(
                        repo = item,
                        onClick = {
                            navigateToDetailsScreen(item.id)
                            Timber.d("ListItemClick: $item")
                        }
                    )
                }
            }
            state.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { PageLoaderAnimation() }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = state.loadState.refresh as LoadState.Error
                        item {
                            ErrorState(
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() }
                            )
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { PageLoaderAnimation() }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = state.loadState.append as LoadState.Error
                        item {
                            ErrorState(
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ListItem(
    repo: RepoViewData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .size(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                ),
            contentAlignment = Alignment.Center
        ) {
            var errorState by remember { mutableStateOf(false) }
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = repo.imageUrl,
                contentDescription = null,
                onError = { errorState = true },
            )
            if (errorState)
                Text(
                    text = repo.title.substring(0..1).uppercase(),
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
        }
        Column(Modifier.weight(1f)) {
            Text(
                text = repo.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = repo.language ?: "",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
        }
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
    }
}

fun generatePreviewRepoData(): List<RepoViewData> {
    return listOf(
        RepoViewData(
            id = 1,
            title = "Android App",
            imageUrl = "https://avatars.githubusercontent.com/u/9919?v=4",
            visibility = Visibility.Public.toString(),
            isPrivate = false,
            language = "Kotlin",
            page = 1,
        ),
        RepoViewData(
            id = 2,
            title = "Compose UI",
            imageUrl = "https://avatars.githubusercontent.com/u/9919?v=4",
            visibility = Visibility.Private.toString(),
            isPrivate = true,
            language = null,
            page = 2,
        ),
        RepoViewData(
            id = 3,
            title = "Networking Library",
            imageUrl = "https://avatars.githubusercontent.com/u/9919?v=4",
            visibility = Visibility.Public.toString(),
            isPrivate = false,
            language = "Java",
            page = 3,
        )
    )
}

@Composable
fun fakePagingItems(): LazyPagingItems<RepoViewData> {
    val fakePagingSource = remember {
        object : PagingSource<Int, RepoViewData>() {
            override fun getRefreshKey(state: PagingState<Int, RepoViewData>): Int? = null

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoViewData> {
                return LoadResult.Page(
                    data = generatePreviewRepoData(),
                    prevKey = null,
                    nextKey = null
                )
            }
        }
    }

    val pager = remember {
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { fakePagingSource }
        )
    }

    return pager.flow.collectAsLazyPagingItems()
}

@PreviewLightDark
@Composable
fun PreviewPagingScreen() {
    val fakeData = fakePagingItems()
    LaunchedEffect(fakeData) {
        fakeData.refresh()
    }
    ABNAmroTheme {
        ReposListContent(
            state = fakeData,
            navigateToDetailsScreen = {},
        )
    }
}
