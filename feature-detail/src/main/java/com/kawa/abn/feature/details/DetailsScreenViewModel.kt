package com.kawa.abn.feature.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kawa.abn.feature.details.domain.GetRepositoryDetails
import com.kawa.abn.foundation.kotlin.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val getRepositoryDetails: GetRepositoryDetails,
    private val mapper: RepositoryItemToRepoDetailsViewDataMapper,
) : ViewModel() {

    private val _state: MutableStateFlow<DetailsScreenState> =
        MutableStateFlow(DetailsScreenState.Loading)
    val state: StateFlow<DetailsScreenState> = _state.asStateFlow()

    fun loadData(id: Int) {
        viewModelScope.launch {
            loadRepo(id)
        }
    }

    @VisibleForTesting
    suspend fun loadRepo(id: Int) {
        _state.value = DetailsScreenState.Loading
        val result = getRepositoryDetails.invoke(id)
            .fold(
                onSuccess = { mapper.map(it) },
                onError = { mapper.map(it) },
            )
        _state.value = result
    }
}

sealed class DetailsScreenState {
    data object Loading : DetailsScreenState()
    data class Success(val data: RepoDetailsViewData) : DetailsScreenState()
    data class Error(val message: String) : DetailsScreenState()
}

data class RepoDetailsViewData(
    val title: String,
    val fullName: String,
    val description: String?,
    val ownerImageUrl: String,
    val visibility: String,
    val isPrivate: Boolean,
    val htmlUrl: String
)
