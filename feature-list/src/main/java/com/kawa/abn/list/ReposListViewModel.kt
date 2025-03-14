package com.kawa.abn.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.kawa.abn.feature.list.domain.usecase.GetListOfReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ReposListViewModel @Inject constructor(
    private val getListOfReposUseCase: GetListOfReposUseCase,
    private val mapper: ReposListScreenStateMapper,
) : ViewModel() {

    val listDataFlow = getListOfReposUseCase()
        .map { it.map { mapper.map(it) } }
        .cachedIn(viewModelScope)
}

data class RepoViewData(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val visibility: String,
    val isPrivate: Boolean,
    val language: String?,
    val page: Int,
)
