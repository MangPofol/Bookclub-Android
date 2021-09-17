package com.example.bookclub.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.bookclub.model.BookModel
import com.example.bookclub.model.KakaoBookModel
import com.example.bookclub.repository.BookRepository
import com.example.bookclub.repository.KakaoBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewModel(application: Application): AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(application)
    private val kakaoBookRepository: KakaoBookRepository = KakaoBookRepository()

//    private val _searchBookTitle: MutableLiveData<String> = MutableLiveData<String>()
    private val _selectedBook: MutableLiveData<KakaoBookModel> = MutableLiveData<KakaoBookModel>()  //사용자가 선택한 책
    private val _searchedBooks: MutableLiveData<MutableList<KakaoBookModel>> = MutableLiveData<MutableList<KakaoBookModel>>()   //검색을 통해 얻어낸 책 목록
    private val _nowBooks: MutableLiveData<MutableList<BookModel>> = MutableLiveData<MutableList<BookModel>>()    //읽는 중 책 목록
    private val _beforeBooks: MutableLiveData<MutableList<BookModel>> = MutableLiveData<MutableList<BookModel>>() //읽고 싶은 책 목록
    private val _afterBooks: MutableLiveData<MutableList<BookModel>> = MutableLiveData<MutableList<BookModel>>()  //완독 책 목록
    private val _readType: MutableLiveData<Int> = MutableLiveData<Int>()    //읽는중, 완독, 읽고싶은 페이지 변환

    private var books: MutableList<BookModel> = ArrayList()

//    private val searchBookTitle : LiveData<String> get() = _searchBookTitle
//    val selectedBook: LiveData<KakaoBookModel> get() = _selectedBook
    val searchedBooks: LiveData<MutableList<KakaoBookModel>> get() = _searchedBooks
    val readType: LiveData<Int> get() = _readType
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

    suspend fun updateSearchBookTitle(title: String) {
        viewModelScope.launch {
            if (title!="")
                _searchedBooks.value = kakaoBookRepository.getKakaoBooks(title, "title", 20)!!.documents
            /*_searchBookTitle.value = title

            if (_searchBookTitle.value!="") {
                _searchedBooks.value = bookRepository.getNaverBooksByTitle(_searchBookTitle.value.toString())
            }*/
        }
    }

    fun updateSelectedBook(position: Int) {
        _selectedBook.value = _searchedBooks.value!![position]
    }

    suspend fun createBook(readType: String): Int? {
        val splitIsbn = _selectedBook.value!!.isbn.split(" ")
        var isbn: String = if (splitIsbn[0]=="") {
            _selectedBook.value!!.isbn.split(" ")[1]
        } else {
            _selectedBook.value!!.isbn.split(" ")[0]
        }

        val book: BookModel = BookModel(
            name=_selectedBook.value!!.title,
            isbn = isbn,
            category = readType
        )

        val code = withContext(Dispatchers.IO) {
            //서버에 책 추가 요청 보냄
            return@withContext bookRepository.createBook(book).code()
        }

        return code
    }

    fun updateReadType(readType: Int) {
        _readType.value = readType
    }
}