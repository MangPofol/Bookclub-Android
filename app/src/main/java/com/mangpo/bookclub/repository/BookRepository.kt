package com.mangpo.bookclub.repository

import android.app.Application
import android.util.Log
import com.google.gson.JsonObject
import com.mangpo.bookclub.dao.BookImageDao
import com.mangpo.bookclub.database.MangpoDatabase
import com.mangpo.bookclub.model.BookImageModel
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.service.BookService
import com.mangpo.bookclub.service.KakaoBookService
import retrofit2.Response
import java.lang.Exception

class BookRepository(
    private val application: Application,
    private val bookService: BookService,
    private val kakaoBookService: KakaoBookService
) {
    private val bookImageDao: BookImageDao

    init {
        val mangpoDB: MangpoDatabase = MangpoDatabase.getInstance(application)!!
        bookImageDao = mangpoDB.bookImageDao()
    }

    suspend fun getBooks(email: String, category: String): MutableList<BookModel> {
        val body = bookService.getBooks(email, category).body()
        var books: MutableList<BookModel> = mutableListOf<BookModel>()

        if (body != null) {
            books = body.books

            for (book in books) {
                var image: String? = bookImageDao.getImage(book.isbn!!)

                if (image == null) {
                    image = kakaoBookService.getKakaoBooks(book.isbn!!, "isbn", 1)
                        .body()!!.documents[0].thumbnail
                    bookImageDao.insertBook(
                        BookImageModel(
                            isbn = book.isbn,
                            image = image
                        )
                    )
                }

                book.imgPath = image
            }
        }

        return books
    }

    suspend fun createBook(book: BookModel): BookModel? {
        try {
            val result = bookService.createBook(book)

            return if (result.isSuccessful) {
                book
            } else {
                Log.e(
                    "BookRepository",
                    "createBook 에러\ncode: ${result.code()}, message: ${result.body().toString()}"
                )

                null
            }
        } catch (e: Exception) {
            e.printStackTrace()

            return null
        }
    }
}