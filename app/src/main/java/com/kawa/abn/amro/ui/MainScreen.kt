package com.kawa.abn.amro.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kawa.abn.foundation.compose.theme.ABNAmroTheme

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()
        Box(modifier = Modifier.padding(innerPadding)) {
            MainScreenContent()
        }
    }
}


@Composable
private fun MainScreenContent() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "list", modifier = Modifier) {
        composable("list") { ReposListScreen() }
//        composable<Friends> { FriendsScreen(...) }
    }
}