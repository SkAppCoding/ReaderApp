package com.example.readerapp.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.readerapp.navigation.ReaderScreens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(navController: NavHostController) {
    Column {
        Text(text = "Home Screen")
        Button(onClick = {
            val auth: FirebaseAuth = Firebase.auth
            auth.signOut()
            navController.navigate(ReaderScreens.LoginScreen.name)
        }) {
            Text(text = "Logout")
        }
    }

}