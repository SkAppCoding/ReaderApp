package com.example.readerapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook
import com.example.readerapp.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: FireRepository
) : ViewModel() {

    var listOfBooks by mutableStateOf(DataOrException<List<MBook>, Boolean, Exception>(listOf(), true, Exception("")))

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            listOfBooks = DataOrException(data = listOf(), loading = true, ex = null)
            try {
                val books = repository.getAllBooksFromDatabase()
                listOfBooks = DataOrException(data = books.data ?: listOf(), loading = false, ex = null)
            } catch (e: Exception) {
                listOfBooks = DataOrException(data = listOf(), loading = false, ex = e)
            }
        }
    }
}