package com.example.readerapp.screens.details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.readerapp.R
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.Item
import com.example.readerapp.utils.getHttpsImageUrl

@Composable
fun BookDetailsScreen(navController: NavController, bookId: String) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = stringResource(R.string.BookDetails),
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
            DetailsContent(navController, bookId)
        }
    }
}

@Composable
fun DetailsContent(
    navController: NavController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val bookInfo = produceState<DataOrException<Item, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true,
                data = null,
                ex = null
            )
        ) {
            value = viewModel.getBookInfo(bookId)
        }.value


        if (bookInfo.loading == true) {
            LinearProgressIndicator()
        } else if (bookInfo.data != null) {
            ShowBookDetails(bookInfo.data!!)
        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Item) {

    val imageUrl: String =
        if (bookInfo.volumeInfo.imageLinks?.smallThumbnail?.isNotEmpty() == true)
            bookInfo.volumeInfo.imageLinks.smallThumbnail
        else
            "https://img-cdn.pixlr.com/image-generator/history/65bb506dcb310754719cf81f/ede935de-1138-4f66-8ed7-44bd16efc709/medium.webp"

    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {

            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(getHttpsImageUrl(imageUrl))
                        .crossfade(true)
                        .build(),
                    contentDescription = "book image",
                    modifier = Modifier

                        .height(150.dp)
                        .padding(15.dp)
                )
            }

            Text(
                text = bookInfo.volumeInfo.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )


            Text(
                text = "Authors: ${bookInfo.volumeInfo.authors?.toString()}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = "Page Count: ${bookInfo.volumeInfo.pageCount}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "Categories: ${bookInfo.volumeInfo.categories?.toString()}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "Published Date: ${bookInfo.volumeInfo.publishedDate}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 5.dp)
            )

            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .padding(top = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        text = "Description: ${bookInfo.volumeInfo.description}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }
    }
}