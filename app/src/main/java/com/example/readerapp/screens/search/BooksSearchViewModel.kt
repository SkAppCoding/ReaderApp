package com.example.readerapp.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

    private var _listOfBooks = MutableStateFlow(
        DataOrException<List<Item>, Boolean, Exception>(listOf(), true, Exception(""))
    )
    var listOfBooks: StateFlow<DataOrException<List<Item>, Boolean, Exception>> = _listOfBooks.asStateFlow()

    private fun updateBooks(query: String) {
        viewModelScope.launch {
            _listOfBooks.value = repository.getBooks(query)
        }
    }

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
            _listOfBooks.value = _listOfBooks.value.copy(loading = true)
            try {
                val books = withContext(Dispatchers.IO) { repository.getBooks(query) }
                _listOfBooks.value = DataOrException(data = books.data, loading = false, ex = null)
            } catch (e: retrofit2.HttpException) { // Catch Retrofit's HttpException
                // Consider keeping previous data: _listOfBooks.value = _listOfBooks.value.copy(loading = false, ex = e)
                _listOfBooks.value = DataOrException(data = listOf(), loading = false, ex = e)
            } catch (e: Exception) {
                // Consider keeping previous data: _listOfBooks.value = _listOfBooks.value.copy(loading = false, ex = e)
                _listOfBooks.value = DataOrException(data = listOf(), loading = false, ex = e)
            }
        }
    }
}