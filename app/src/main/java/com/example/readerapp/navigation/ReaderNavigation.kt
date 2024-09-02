package com.example.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.readerapp.screens.SplashScreen
import com.example.readerapp.screens.details.BookDetailsScreen
import com.example.readerapp.screens.home.HomeScreen
import com.example.readerapp.screens.login.LoginScreen
import com.example.readerapp.screens.search.BookSearchViewModel
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
            val viewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController = navController, viewModel)
        }

        composable(route = ReaderScreens.DetailScreen.name) {
            BookDetailsScreen(navController = navController)
        }

        composable(route = ReaderScreens.UpdateScreen.name) {
            UpdateScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            StatsScreen(navController = navController)
        }

    }
}