package com.example.readerapp.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.readerapp.R
import com.example.readerapp.components.FABContent
import com.example.readerapp.components.InputField
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.model.MBook
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.home.HomeContent

@Preview
@Composable
fun SearchScreen(navController: NavController = NavController(context = LocalContext.current)) {
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
fun SearchContent(navController: NavController) {
    SearchForm(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        navController = navController
    ) { query ->
        navController.navigate(ReaderScreens.DetailScreen.name + "/$query")
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

        val listOfBooks = listOf(
            MBook(id = "Book 1", title = "Hello Again", authors = "Stefan of us", published_date = "2023.09.08" ),
            MBook(id = "Book 2", title = "Hello", authors = "All of ", published_date = "2024.01.02" ),
            MBook(id = "Book 3", title = "Hello Again", authors = "All of us", published_date = "2023.09.08" ),
            MBook(id = "Book 4", title = "Hello", authors = "All of ", published_date = "2024.01.02" ),
            MBook(id = "Book 5", title = "Hello Again", authors = "All of us", published_date = "2023.09.08" ),
            MBook(id = "Book 6", title = "Hello", authors = "All of ", published_date = "2024.01.02" ),


            )

        Spacer(modifier = Modifier.height(5.dp))

        SearchResults(listOfBooks, navController = navController)

    }
}

@Composable
fun SearchResults(books: List<MBook>, navController: NavController) {
        LazyColumn(modifier = Modifier.padding(start = 20.dp, end = 20.dp).fillMaxSize()) {
        books.forEach { book ->
            item {
                SearchResultItem(book, navController =  navController)
            }
        }
    }
}

@Preview
@Composable
fun SearchResultItem(
    book: MBook = MBook(
        id = "1",
        title = "Hello Again",
        authors = "All of us",
        notes = "This is a note",
        published_date = "2023.09.08"
    ),
    onPress: () -> Unit = {},
    navController: NavController = NavController(context = LocalContext.current)
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = { onPress() }
    ) {
        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 5.dp)) {
            AsyncImage(
                model = "https://img-cdn.pixlr.com/image-generator/history/65bb506dcb310754719cf81f/ede935de-1138-4f66-8ed7-44bd16efc709/medium.webp",
                contentDescription = "book image",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .padding(4.dp),
            )

            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = 5.dp)) {

                Text(
                    text = book.title.toString(),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${stringResource(R.string.Author)}: ${book.authors.toString()}",
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${stringResource(R.string.Date)}: ${book.published_date.toString()}",
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "[Computers]",
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}