package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.BookResData
import retrofit2.Response
import retrofit2.http.*

interface BookService {
    //books?email=&category=
    @GET("books")
    suspend fun getBooks(
        @Query("email") email: String,
        @Query("category") category: String
    ): Response<BookResData>

    @Headers("Content-Type: application/json")
    @POST("/books")
    suspend fun createBook(@Body newBook: BookModel): Response<BookModel>
}