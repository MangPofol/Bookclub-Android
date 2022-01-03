package com.mangpo.bookclub.repository

import android.app.Application
import android.util.Log
import com.google.gson.*
import com.mangpo.bookclub.dao.BookImageDao
import com.mangpo.bookclub.database.MangpoDatabase
import com.mangpo.bookclub.model.BookImageModel
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.BooksModel
import com.mangpo.bookclub.service.BookService
import com.mangpo.bookclub.service.KakaoBookService
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

    suspend fun getBooks(category: String): HashMap<String, Any> {
        val reqHm = HashMap<String, Any>()
        val result = bookService.getBooks(category)

        reqHm["code"] = result.code()
        if (result.isSuccessful) {
            Log.d(
                "BookRepository",
                "getBooks is Successful!\ncode: ${result.code()}, body: ${result.body()}"
            )

            when (result.code()) {
                200 -> {
                    if (result.body() != null) {
                        reqHm["books"] = getBookImg(result.body()!!)
                    }

                }
            }
        } else {
            Log.e(
                "BookRepository",
                "getBooks is not Successful!\ncode: ${result.code()}, message: ${result.message()}"
            )
        }

        return reqHm
    }

    suspend fun createBook(book: BookModel): HashMap<String, Any>? {
        try {
            val result = bookService.createBook(book)
            val hm: HashMap<String, Any> = hashMapOf()

            if (result.isSuccessful) {
                hm["code"] = result.code()

                when (result.code()) {
                    201 -> {
                        val body = result.body()!!
                        body.imgPath = book.imgPath
                        hm["book"] = body
                    }
                    else -> {
                        Log.e(
                            "BookRepository",
                            "createBook 에러\ncode: ${result.code()}, message: ${
                                result.body().toString()
                            }"
                        )
                    }
                }
            } else {
                Log.e(
                    "BookRepository",
                    "createBook 에러\ncode: ${result.code()}, message: ${result.body().toString()}"
                )

                hm["code"] = result.code()
            }

            return hm
        } catch (e: Exception) {
            e.printStackTrace()

            return null
        }
    }

    suspend fun updateBook(bookId: Long, categoryJson: JsonObject): Boolean {
        val result = bookService.updateBook(bookId, categoryJson)

        return if (result.isSuccessful) {
            true
        } else {
            Log.e(
                "BookRepository",
                "updateBook error!\n code: ${result.code()}\nerror message: ${result.message()}"
            )
            false
        }
    }

    //응답받은 books 데이터를 통해 카카오 도서 API 를 통해서 이미지 path 가져오는 함수
    private suspend fun getBookImg(books: BooksModel): BooksModel {
        for (book in books.books) {
            var image: String = bookImageDao.getImage(book.isbn!!)

            if (image == null) {
                try {
                    image = kakaoBookService.getKakaoBooks(book.isbn!!, "isbn", 1)
                        .body()!!.documents[0].thumbnail!!
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                bookImageDao.insertBook(
                    BookImageModel(
                        isbn = book.isbn,
                        image = image
                    )
                )
            }

            book.imgPath = image
        }

        return books
    }
}