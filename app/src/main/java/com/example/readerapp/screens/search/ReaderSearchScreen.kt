package com.example.readerapp.screens.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.readerapp.R
import com.example.readerapp.components.InputField
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.model.Item
import com.example.readerapp.utils.getHttpsImageUrl

@Composable
fun SearchScreen(navController: NavController, viewModel: BooksSearchViewModel = hiltViewModel()) {
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
            SearchContent(navController)
        }
    }
}

@Composable
fun SearchContent(navController: NavController, viewModel: BooksSearchViewModel = hiltViewModel()) {

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
fun SearchResults(navController: NavController, viewModel: BooksSearchViewModel = hiltViewModel()) {

    val listOfBooks by viewModel.listOfBooks.collectAsState()

    if (listOfBooks.loading == true) {
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
            listOfBooks.data?.forEach { book ->
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

            val imageUrl: String = book.volumeInfo.imageLinks.smallThumbnail.ifEmpty{ "https://img-cdn.pixlr.com/image-generator/history/65bb506dcb310754719cf81f/ede935de-1138-4f66-8ed7-44bd16efc709/medium.webp" }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getHttpsImageUrl(imageUrl))
                    .crossfade(true)
                    .build(),
                contentDescription = "book image",
                modifier = Modifier
                    .width(100.dp)
                    .heightIn(100.dp)
                    .padding(4.dp)
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