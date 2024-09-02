package com.example.readerapp.screens.home

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.readerapp.R
import com.example.readerapp.components.FABContent
import com.example.readerapp.components.ListCard
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.components.TitleSection
import com.example.readerapp.model.MBook
import com.example.readerapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        ReaderAppBar(title = "Reader App", navController = navController)
    }, floatingActionButton = {
        FABContent {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeContent(navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavController) {

    val listOfBooks = listOf(
        MBook(id = "Book 1", title = "Hello Again", authors = "Stefan of us"),
        MBook(id = "Book 2", title = "Hello", authors = "All of "),
        MBook(id = "Book 3", title = "Again", authors = "All  us"),
        MBook(id = "Book 4", title = "Hello Again", authors = "All of us"),
        MBook(id = "Book 5", title = "Hello Again", authors = "STefan of us"),
        MBook(id = "Book 6", title = "Hello Again", authors = "All of us"),

    )

//    val email = FirebaseAuth.getInstance().currentUser?.email
//    val currentUserName = if (!email.isNullOrEmpty())
//        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else "N/A"

    Column(
        modifier = Modifier
            .padding(2.dp)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(
                    start = 15.dp,
                    end = 15.dp
                )
        ) {
            TitleSection(label = stringResource(R.string.CurrentReadingsTitle))

            Spacer(modifier = Modifier.weight(1f))
        }
        HorizontalDivider()
        ReadingRightNowArea(
            books = listOf(),
            navController = navController
        )


        Row(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(
                    top = 25.dp,
                    start = 15.dp,
                    end = 15.dp
                )
        ) {
            TitleSection(label = stringResource(R.string.ReadingListTitle))
        }
        HorizontalDivider()

        BoolListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BoolListArea(
    listOfBooks: List<MBook>,
    navController: NavController
) {
    HorizontalScrollableComponent(listOfBooks) {
        Log.d("TAG", "BoolListArea: $it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    onCardPressed: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)
    ){

        for (book in listOfBooks) {
            ListCard(book){
                onCardPressed(it)
            }
        }
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
    ListCard()
}