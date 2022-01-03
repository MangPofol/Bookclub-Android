package com.mangpo.bookclub.service

import com.google.gson.JsonObject
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.BooksModel
import retrofit2.Response
import retrofit2.http.*

interface BookService {
    @GET("/books")
    suspend fun getBooks(
        @Query("category") category: String
    ): Response<BooksModel>

    @Headers("Content-Type: application/json")
    @POST("/books")
    suspend fun createBook(@Body newBook: BookModel): Response<BookModel>

    @PATCH("/books/{bookId}")
    suspend fun updateBook(
        @Path("bookId") bookId: Long,
        @Body categoryJson: JsonObject
    ): Response<Int>
}