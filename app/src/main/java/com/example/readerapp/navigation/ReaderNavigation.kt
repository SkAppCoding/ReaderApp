package com.example.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readerapp.screens.SplashScreen
import com.example.readerapp.screens.details.BookDetailsScreen
import com.example.readerapp.screens.home.HomeScreen
import com.example.readerapp.screens.home.HomeScreenViewModel
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

        composable(route = ReaderScreens.ReaderHomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewModel)
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

        val readerName = ReaderScreens.UpdateScreen.name
        composable(
            route = "$readerName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId"){ type = NavType.StringType })
        ) {navBackStackEntry ->
            navBackStackEntry.arguments?.getString("bookItemId").let {
                UpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }

        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            StatsScreen(navController = navController)
        }

    }
}