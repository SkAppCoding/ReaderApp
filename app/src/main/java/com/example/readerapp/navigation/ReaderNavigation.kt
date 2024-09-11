package com.example.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readerapp.screens.SplashScreen
import com.example.readerapp.screens.details.BookDetailsScreen
import com.example.readerapp.screens.home.HomeScreen
import com.example.readerapp.screens.login.LoginScreen
import com.example.readerapp.screens.search.BooksSearchViewModel
import com.example.readerapp.screens.search.SearchScreen
import com.example.readerapp.screens.stats.StatsScreen
import com.example.readerapp.screens.update.UpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {

        composable(route = ReaderScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(route = ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

//        composable(route = ReaderScreens.CreateAccountScreen.name) {
//            CreateAccountScreen(navController = navController)
//        }

        composable(route = ReaderScreens.ReaderHomeScreen.name) {
            HomeScreen(navController = navController)
        }

        composable(route = ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BooksSearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }

        val detailName = ReaderScreens.DetailScreen.name
        composable(
            route = "$detailName/{bookId}", arguments =
            listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        composable(route = ReaderScreens.UpdateScreen.name) {
            UpdateScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            StatsScreen(navController = navController)
        }

    }
}