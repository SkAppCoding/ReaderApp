package com.example.readerapp.screens.details

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.readerapp.R
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook
import com.example.readerapp.utils.getHttpsImageUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

            LazyColumn(modifier = Modifier.padding(10.dp)) {
                item {
                    ShowBookDetails(bookInfo.data!!)

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {

                        Button(
                            onClick = {
                                //save this book to ghe firestore database

                                val book = MBook(
                                    title = bookInfo.data!!.volumeInfo.title?.toString(),
                                    authors = bookInfo.data!!.volumeInfo.authors?.toString(),
                                    description = bookInfo.data!!.volumeInfo.description?.toString(),
                                    categories = bookInfo.data!!.volumeInfo.categories?.toString(),
                                    notes = "",
                                    photoUrl = bookInfo.data!!.volumeInfo.imageLinks?.thumbnail,
                                    publishedDate = bookInfo.data!!.volumeInfo.publishedDate?.toString(),
                                    pageCount = bookInfo.data!!.volumeInfo.pageCount?.toString(),
                                    rating = 0.0,
                                    googleBookId = bookId,
                                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                )

                                saveToFirebase(book, navController = navController)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.Save),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Button(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.Cancel),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}

fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                //Get created Document and add id to it
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnSuccessListener {
                        navController.popBackStack()
                    }
                    .addOnFailureListener{
                        Log.w("Error", "SaveToFirebase: Error updating doc", it)
                    }
            }
    } else {

    }
}


@Composable
fun ShowBookDetails(bookInfo: Item) {

    val imageUrl: String =
        if (bookInfo.volumeInfo.imageLinks?.smallThumbnail?.isNotEmpty() == true)
            bookInfo.volumeInfo.imageLinks.smallThumbnail
        else
            "https://img-cdn.pixlr.com/image-generator/history/65bb506dcb310754719cf81f/ede935de-1138-4f66-8ed7-44bd16efc709/medium.webp"

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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(top = 10.dp)
            .heightIn(50.dp, 300.dp)
    ) {

        val cleanDescription = HtmlCompat.fromHtml(
            bookInfo.volumeInfo!!.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()

        LazyColumn(modifier = Modifier.padding(15.dp)) {
            item {
                Text(
                    text = "Description: $cleanDescription",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}
