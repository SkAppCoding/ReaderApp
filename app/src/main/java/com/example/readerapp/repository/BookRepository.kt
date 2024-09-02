package com.example.readerapp.repository

import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.Item
import com.example.readerapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()

    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
        } catch (ex: Exception) {
            dataOrException.ex = ex
        }

        return dataOrException
    }

    suspend fun getBookInfo(bookId: String) : DataOrException<Item, Boolean, Exception>{
        val response = try {
            bookInfoDataOrException.loading = true
            api.getBookInfo(bookId)

            if (bookInfoDataOrException.data.toString().isNotEmpty()) bookInfoDataOrException.loading = false
            else {}

        } catch (ex: Exception) {
            dataOrException.ex = ex
        }
        return bookInfoDataOrException
    }

}