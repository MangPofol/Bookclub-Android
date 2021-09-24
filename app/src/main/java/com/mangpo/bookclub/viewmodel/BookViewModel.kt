package com.mangpo.bookclub.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.KakaoBookModel
import com.mangpo.bookclub.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(application)

    private val _selectedBook: MutableLiveData<KakaoBookModel> =
        MutableLiveData<KakaoBookModel>()  //사용자가 선택한 책
    private val _nowBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>()    //읽는 중 책 목록
    private val _beforeBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>() //읽고 싶은 책 목록
    private val _afterBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>()  //완독 책 목록

    private var books: MutableList<BookModel> = ArrayList()

    val selectedBook: LiveData<KakaoBookModel> get() = _selectedBook
    val nowBooks: LiveData<MutableList<BookModel>> get() = _nowBooks
    val beforeBooks: LiveData<MutableList<BookModel>> get() = _beforeBooks
    val afterBooks: LiveData<MutableList<BookModel>> get() = _afterBooks


    suspend fun getBooks(category: String): MutableList<BookModel>? {
        books = withContext(Dispatchers.IO) {
            bookRepository.getBooks("", category)
        }

        return when (category) {
            "NOW" -> {
                _nowBooks.value = books
                _nowBooks.value
            }
            "AFTER" -> {
                _afterBooks.value = books
                _afterBooks.value
            }
            "BEFORE" -> {
                _beforeBooks.value = books
                _beforeBooks.value
            }

            else -> null
        }
    }

    fun updateBeforeBook(book: BookModel) {
        books = _beforeBooks.value!!
        books.add(book)
        _beforeBooks.postValue(books)
    }

    /*fun clearSelectedBook() {
        _selectedBook.value = _selectedBook.value!!.copy("", "", "")
    }*/

    /*private fun updateBooks(book: BookModel) {
        when (book.category) {
            "NOW" -> {
                books = _nowBooks.value!!
                books.add(book)
                _nowBooks.postValue(books)
            }
            "AFTER" -> {
                books = _afterBooks.value!!
                books.add(book)
                _afterBooks.postValue(books)
            }
            "BEFORE" -> {
                books = _beforeBooks.value!!
                books.add(book)
                _beforeBooks.postValue(books)
            }
        }
    }*/
}