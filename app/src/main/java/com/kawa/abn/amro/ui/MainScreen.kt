package com.kawa.abn.amro.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kawa.abn.feature.details.DetailsScreen
import com.kawa.abn.foundation.compose.theme.navigation.openUrl
import com.kawa.abn.list.ReposListScreen

@Composable
fun MainScreen() {
    val context = LocalContext.current

    MainScreenContent(
        navigateToUrl = { context.openUrl(it) }
    )
}

@Composable
private fun MainScreenContent(navigateToUrl: (String) -> Unit) {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = "list",
    ) {
        composable("list") {
            ReposListScreen(
                navigateToDetailsScreen = {
                    navController.navigate("details/$it")
                }
            )
        }
        composable("details/{id}") {
            val repoId = it.arguments?.getString("id")?.toInt() ?: error("id is missing")
            DetailsScreen(
                id = repoId,
                onUrlClick = navigateToUrl,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
