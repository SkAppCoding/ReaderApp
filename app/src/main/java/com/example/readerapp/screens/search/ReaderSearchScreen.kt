package com.example.readerapp.screens.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.readerapp.R
import com.example.readerapp.components.InputField
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook

@Composable
fun SearchScreen(navController: NavController, viewModel: BookSearchViewModel) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = stringResource(R.string.SearchBooks),
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            navController = navController,
            showProfile = false,
            onBackArrowClicked = {
                navController.popBackStack()
            }
        )
    })
    {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SearchContent(navController, viewModel)
        }
    }
}

@Composable
fun SearchContent(navController: NavController, viewModel: BookSearchViewModel) {

    Column {
        SearchForm(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            navController = navController,
        ) { query ->
            viewModel.searchBooks(query)
        }

        Spacer(modifier = Modifier.height(5.dp))

        SearchResults(navController = navController)
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = stringResource(R.string.Search),
    navController: NavController,
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun SearchResults(navController: NavController) {

    val viewModel: BookSearchViewModel = hiltViewModel()
    val listOfBooks = viewModel.listOfBooks.value.data

    if (viewModel.listOfBooks.value.loading == true) {
        Row(
            modifier = Modifier.padding(end = 2.dp). fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator()
        }

    } else {

        LazyColumn(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize()) {
            listOfBooks?.forEach { book ->
                item {
                    SearchResultItem(book, navController = navController)
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    book: Item,
    onPress: () -> Unit = {},
    navController: NavController = NavController(context = LocalContext.current)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = { onPress() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 5.dp)
        ) {

            val imageUrl: String = book.volumeInfo.imageLinks.smallThumbnail.ifEmpty{ "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80" }

            AsyncImage(
                model = imageUrl,
                contentDescription = "book image",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .padding(4.dp),
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(5.dp)
            ) {

                Text(
                    text = book.volumeInfo.title,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "${stringResource(R.string.Author)}: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "${stringResource(R.string.Date)}: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = book.volumeInfo.categories.toString(),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}