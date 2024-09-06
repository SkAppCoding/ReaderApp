package com.example.readerapp.repository

import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.Item
import com.example.readerapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {
        val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            val book = api.getAllBooks(searchQuery) // Get the Book object
            dataOrException.data = book?.items ?: emptyList() // Handle null book or items safely
            dataOrException.loading = false
        } catch (ex: Exception) {
            dataOrException.ex = ex
            dataOrException.loading = false
        }
        return dataOrException
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception> {
        val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>() // Create a new instance for each call
        try {
            bookInfoDataOrException.loading = true
            val bookInfo = api.getBookInfo(bookId) // Get the response
            bookInfoDataOrException.data = bookInfo // Update the data
            bookInfoDataOrException.loading = false
        } catch (ex: Exception) {
            bookInfoDataOrException.ex = ex
            bookInfoDataOrException.loading = false // Set loading to false in case of an exception
        }
        return bookInfoDataOrException
    }
}