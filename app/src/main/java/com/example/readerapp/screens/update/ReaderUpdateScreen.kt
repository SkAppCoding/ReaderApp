package com.example.readerapp.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.readerapp.R
import com.example.readerapp.components.FABContent
import com.example.readerapp.components.InputField
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.MBook
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.home.HomeContent
import com.example.readerapp.screens.home.HomeScreenViewModel
import com.example.readerapp.utils.getHttpsImageUrl

@Composable
fun UpdateScreen(
    navController: NavHostController,
    bookItemId: String
) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = stringResource(R.string.UpdateBook),
            navController = navController,
            showProfile = false,
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            onBackArrowClicked = { navController.popBackStack() }
        )
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            UpdateScreenContent(navController, bookItemId = bookItemId)
        }
    }
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun UpdateScreenContent(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    bookItemId: String
) {

    val bookInfo = produceState<DataOrException<List<MBook>, Boolean, Exception>>(
        initialValue = DataOrException(data = emptyList(), loading = true, ex = null)
    ) {
        value = viewModel.listOfBooks
    }.value

    Column(
        modifier = Modifier
            .padding(top = 3.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Log.d("UpdateScreenContent", "UpdateScreenContent: ${viewModel.listOfBooks.data}")
        if (bookInfo.loading == true) {
            Row(
                modifier = Modifier
                    .padding(end = 2.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator()
                bookInfo.loading = false
            }
        } else {
            ShowBookUpdate(bookInfo = viewModel.listOfBooks, bookItemId = bookItemId)
        }


        val book = viewModel.listOfBooks?.data?.firstOrNull { mBook ->
            mBook.googleBookId == bookItemId
        }
        if (book != null) {
            ShowSimpleForm(book, navController)
        } else {
            // Handle the case where no book is found
            // e.g., show an error message or navigate back
        }
    }
}

@Composable
fun ShowSimpleForm(book: MBook, navController: NavHostController) {

    val notesText = remember { mutableStateOf("") }
    SimpleForm(
        defaultValue = book.notes.toString().ifEmpty { stringResource(R.string.NoThoughtsAvailable) },
        modifier = Modifier
    ) { note ->
        notesText.value = note
    }
}

@Composable
fun SimpleForm(
    modifier: Modifier,
    loading: Boolean = false,
    defaultValue: String,
    onSearch: (String) -> Unit = {}
) {
    Column {
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) { textFieldValue.value.trim().isNotEmpty() }

        InputField(
            modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(MaterialTheme.colorScheme.background, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = stringResource(R.string.EnterYourThoughts),
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String
) {
    Row {
        if (bookInfo.data != null) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                CardListItem(book = bookInfo.data!!.first { mBook ->
                    mBook.googleBookId == bookItemId
                }, onPress = { })
            }
        }
    }
}

@Composable
fun CardListItem(book: MBook, onPress: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clickable { },
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getHttpsImageUrl(book.photoUrl.toString()))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .height(140.dp)
                    .width(100.dp)
                    .padding(10.dp)
            )

            Column {
                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp)
                )
            }
        }
    }
}