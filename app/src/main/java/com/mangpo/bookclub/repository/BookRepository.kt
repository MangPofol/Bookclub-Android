package com.mangpo.bookclub.repository

import android.app.Application
import com.mangpo.bookclub.dao.BookImageDao
import com.mangpo.bookclub.database.MangpoDatabase
import com.mangpo.bookclub.model.BookImageModel
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.service.BookService
import com.mangpo.bookclub.service.KakaoBookService
import retrofit2.Response

class BookRepository(application: Application) {
    private val bookService: BookService = ApiClient.bookService
    private val bookImageDao: BookImageDao
    private val kakaoBookService: KakaoBookService = ApiClient.kakaoBookService

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
                            isbn = book.isbn.toString(),
                            image = image
                        )
                    )
                }

                book.image = image
            }
        }

        return books
    }

    suspend fun createBook(book: BookModel): Response<BookModel> {
        return bookService.createBook(book)
    }
}