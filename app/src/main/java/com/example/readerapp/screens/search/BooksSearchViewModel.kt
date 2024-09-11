package com.example.readerapp.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.Item
import com.example.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BooksSearchViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    var listOfBooks by mutableStateOf(DataOrException<List<Item>, Boolean, Exception>(listOf(), true, Exception("")))

    init {
        loadBooks()
    }

    private fun loadBooks(){
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                return@launch
            }
            listOfBooks = DataOrException(loading = true)
            try {
                val books = withContext(this.coroutineContext) { repository.getBooks(query) }
                Log.d("BooksSearchViewModel", "Books: ${books.data}")
                listOfBooks = DataOrException(data = books.data ?: listOf(), loading = false, ex = null)
            } catch (e: retrofit2.HttpException) {
                listOfBooks = DataOrException(data = listOf(), loading = false, ex = e)
            } catch (e: Exception) {
                listOfBooks = DataOrException(data = listOf(), loading = false, ex = e)
            }
        }
    }
}