package com.mangpo.bookclub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.KakaoBookModel
import com.mangpo.bookclub.repository.BookRepository
import com.mangpo.bookclub.repository.KakaoBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewModel(
    application: Application,
    private val bookRepository: BookRepository,
    private val kakaoBookRepository: KakaoBookRepository
) : AndroidViewModel(application) {

    private val _searchedBooks: MutableLiveData<MutableList<KakaoBookModel>> =
        MutableLiveData<MutableList<KakaoBookModel>>()   //검색을 통해 얻어낸 책 목록
    private val _book: MutableLiveData<BookModel> = MutableLiveData<BookModel>()
    private val _nowBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>()    //읽는 중 책 목록
    private val _beforeBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>() //읽고 싶은 책 목록
    private val _afterBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>()  //완독 책 목록
    private val _readType: MutableLiveData<String> = MutableLiveData<String>()
    private val _myLibrarySearch: MutableLiveData<String> = MutableLiveData<String>()
    private val _myLibrarySort: MutableLiveData<String> = MutableLiveData<String>()

    private var _email: String = ""

    val book: LiveData<BookModel> get() = _book
    val nowBooks: LiveData<MutableList<BookModel>> get() = _nowBooks
    val beforeBooks: LiveData<MutableList<BookModel>> get() = _beforeBooks
    val afterBooks: LiveData<MutableList<BookModel>> get() = _afterBooks
    val searchedBooks: LiveData<MutableList<KakaoBookModel>> get() = _searchedBooks
    val readType: LiveData<String> get() = _readType
    val myLibrarySearch: LiveData<String> get() = _myLibrarySearch
    val myLibrarySort: LiveData<String> get() = _myLibrarySort

    fun getBookList(category: String): MutableList<BookModel>? {
        return when (category) {
            "NOW" -> _nowBooks.value
            "AFTER" -> _afterBooks.value
            else -> _beforeBooks.value
        }
    }

    fun getBook(): BookModel? = _book.value

    fun setBook(book: BookModel) {
        _book.value = book
    }

    fun setReadType(readType: String) {
        _readType.value = readType
    }

    fun setMyLibrarySearch(search: String) {
        _myLibrarySearch.value = search
    }

    fun setMyLibrarySort(filter: String) {
        _myLibrarySort.value = filter
    }

    suspend fun requestBookList(email: String, category: String) {
        _email = email

        val books = withContext(Dispatchers.IO) {
            bookRepository.getBooks(email, category)
        }

        when (category) {
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

    suspend fun getSearchedBooks(title: String) {
        viewModelScope.launch {
            _searchedBooks.value = kakaoBookRepository.getKakaoBooks(title, "title", 20)!!.documents
        }

    }

    suspend fun createBook(book: BookModel): Int? {
        if (book.isbn!!.split(" ").size != 1) {
            book.isbn = book.isbn!!.split(" ")[1]
        }

        val hashMap = viewModelScope.async {
            bookRepository.createBook(book)
        }

        return if (hashMap.await() != null) {
            val code = hashMap.await()!!["code"] as Int

            if (code == 201) {
                _book.value = hashMap.await()!!["book"] as BookModel
                requestBookList(_email, _book.value!!.category)
            }

            code
        } else {
            null
        }
    }

    suspend fun updateBook(bookId: Long, category: String): Boolean {
        val categoryObj: JsonObject = JsonObject()
        categoryObj.addProperty("category", category)

        val result = viewModelScope.async {
            bookRepository.updateBook(bookId, categoryObj)
        }

        return result.await()
    }
}