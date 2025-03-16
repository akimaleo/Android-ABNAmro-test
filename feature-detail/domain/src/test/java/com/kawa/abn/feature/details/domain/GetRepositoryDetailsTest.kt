package com.kawa.abn.feature.details.domain

import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.feature.list.domain.repository.ListRepository
import com.kawa.abn.foundation.kotlin.error
import com.kawa.abn.foundation.kotlin.success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

class GetRepositoryDetailsTest {
    private val repoId: Int = 0
    private val listRepository: ListRepository = mockk()
    private val getRepositoryDetails = GetRepositoryDetails(
        listRepository = listRepository,
    )

    @Test
    fun `test GetRepositoryDetails returns RepositoryItem if repository response is success`() =
        runTest {
            val mockRepositoryItem = mockk<RepositoryItem>()
            coEvery { listRepository.getRepo(repoId) } returns mockRepositoryItem.success()
            assertEquals(getRepositoryDetails.invoke(repoId), mockRepositoryItem.success())
        }

    @Test
    fun `test GetRepositoryDetails returns Throwable error if repository response is error`() =
        runTest {
            val mockThrowable = mockk<Throwable>()
            coEvery { listRepository.getRepo(repoId) } returns mockThrowable.error()
            assertEquals(getRepositoryDetails.invoke(repoId), mockThrowable.error())
        }
}