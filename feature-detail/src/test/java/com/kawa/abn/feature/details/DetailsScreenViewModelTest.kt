package com.kawa.abn.feature.details

import app.cash.turbine.test
import com.kawa.abn.feature.details.domain.GetRepositoryDetails
import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.foundation.kotlin.error
import com.kawa.abn.foundation.kotlin.success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsScreenViewModelTest {
    private val repoId = 1
    private val getRepositoryDetails: GetRepositoryDetails = mockk()
    private val mapper: RepositoryItemToRepoDetailsViewDataMapper = mockk()

    private val viewModel = DetailsScreenViewModel(
        getRepositoryDetails = getRepositoryDetails,
        mapper = mapper
    )

    @Test
    fun `initial state should be Loading`() = runTest {
        assertEquals(DetailsScreenState.Loading, viewModel.state.first())
    }

    @Test
    fun `loadData should update state with content when repository details fetch succeeds`() =
        runTest {
            val mockRepositoryItem = mockk<RepositoryItem>()
            val mockRepoDetailsViewData = mockk<RepoDetailsViewData>()
            coEvery { getRepositoryDetails.invoke(repoId) } returns mockRepositoryItem.success()
            every { mapper.map(mockRepositoryItem) } returns DetailsScreenState.Success(
                mockRepoDetailsViewData
            )

            // When
            viewModel.loadRepo(repoId)

            // Verify
            viewModel.state.test {
                assertEquals(
                    DetailsScreenState.Success(mockRepoDetailsViewData),
                    expectMostRecentItem()
                )
            }
            coVerify(exactly = 1) { getRepositoryDetails.invoke(repoId) }
            verify(exactly = 1) { mapper.map(mockRepositoryItem) }
        }

    @Test
    fun `loadData should update state with Error when repository details fetch fails`() = runTest {
        val errorMessage = "Network Error"

        coEvery { getRepositoryDetails.invoke(repoId) } returns Exception(errorMessage).error()
        every { mapper.map(any<Throwable>()) } returns DetailsScreenState.Error(errorMessage)

        // When
        viewModel.loadRepo(repoId)

        // Verify
        viewModel.state.test {
            assertEquals(DetailsScreenState.Error(errorMessage), expectMostRecentItem())
        }
        coVerify(exactly = 1) { getRepositoryDetails.invoke(repoId) }
        verify(exactly = 1) { mapper.map(any<Throwable>()) }
    }
}