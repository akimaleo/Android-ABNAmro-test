package com.kawa.abn.feature.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kawa.abn.foundation.compose.theme.theme.ABNAmroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loadingState_isDisplayed() {
        composeTestRule.setContent {
            ABNAmroTheme {
                DetailsScreenContent(
                    state = DetailsScreenState.Loading,
                    onBackClick = {},
                    onRetryClick = {},
                    onUrlClick = {}
                )
            }
        }

        // Verify that CircularProgressIndicator (loading) is shown
        composeTestRule.onNode(hasTestTag("LoadingIndicator")).assertIsDisplayed()
    }

    @Test
    fun errorState_isDisplayed_andRetryButtonWorks() {
        var retryClicked = false
        composeTestRule.setContent {
            ABNAmroTheme {
                DetailsScreenContent(
                    state = DetailsScreenState.Error("Network Error"),
                    onBackClick = {},
                    onRetryClick = { retryClicked = true },
                    onUrlClick = {}
                )
            }
        }

        // Verify Error message
        composeTestRule.onNodeWithText("Network Error").assertIsDisplayed()

        // Verify retry button exists and click it
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryClicked)
    }

    @Test
    fun successState_isDisplayed_andOpenGitHubWorks() {
        val repo = RepoDetailsViewData(
            title = "Jetpack Compose",
            fullName = "android/compose",
            description = "Android’s modern toolkit for building native UI.",
            ownerImageUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
            visibility = "Public",
            isPrivate = false,
            htmlUrl = "https://github.com/android/compose"
        )

        var clickedUrl: String? = null
        composeTestRule.setContent {
            ABNAmroTheme {
                DetailsScreenContent(
                    state = DetailsScreenState.Success(repo),
                    onRetryClick = {},
                    onBackClick = {},
                    onUrlClick = { clickedUrl = it }
                )
            }
        }

        // Verify Repository Name
        composeTestRule.onNodeWithText("android/compose").assertIsDisplayed()

        // Verify Open in GitHub button and click it
        composeTestRule.onNodeWithText("Open in GitHub").performClick()
        assert(clickedUrl.equals(repo.htmlUrl))
    }
}
