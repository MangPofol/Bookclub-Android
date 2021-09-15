package com.example.bookclub.service

import com.example.bookclub.BuildConfig
import com.example.bookclub.model.BookModel
import com.example.bookclub.model.BookResData
import retrofit2.Response
import retrofit2.http.*

interface BookService {
    //books?email=&category=
    @GET("books")
    suspend fun getBooks(
        @Query("email") email: String,
        @Query("category") category: String
    ): Response<BookResData>

    //?d_isbn=
    @Headers(
        "X-Naver-Client-Id: ${BuildConfig.NAVERBOOK_KEY}",
        "X-Naver-Client-Secret: ${BuildConfig.NAVERBOOK_SECRET}"
    )
    @GET("book_adv.xml")
    suspend fun getNaverBooksByIsbn(@Query("d_isbn") isbn: String): Response<String>

    //d_titl=
    @Headers(
        "X-Naver-Client-Id: ${BuildConfig.NAVERBOOK_KEY}",
        "X-Naver-Client-Secret: ${BuildConfig.NAVERBOOK_SECRET}"
    )
    @GET("book_adv.xml?display=25")
    suspend fun getNaverBooksByTitle(@Query("d_titl") title: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("/books")
    suspend fun createBook(@Body newBook: BookModel): Response<BookModel>
}