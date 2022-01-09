package com.mangpo.bookclub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.BooksModel
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
    private val _getBooksCode: MutableLiveData<Int> = MutableLiveData<Int>()    //getBooks req 응답코드
    private val _searchFilterBtnClick: MutableLiveData<Int> = MutableLiveData<Int>()  //내서재에서 검색 필터 버튼이 눌렸는지 안눌렸는지 확인용 변수
    private val _sortFilterBtnClick: MutableLiveData<Int> = MutableLiveData<Int>()  //내서재에서 정렬 필터 버튼이 눌렸는지 안눌렸는지 확인용 변수

    val book: LiveData<BookModel> get() = _book
    val nowBooks: LiveData<MutableList<BookModel>> get() = _nowBooks
    val beforeBooks: LiveData<MutableList<BookModel>> get() = _beforeBooks
    val afterBooks: LiveData<MutableList<BookModel>> get() = _afterBooks
    val searchedBooks: LiveData<MutableList<KakaoBookModel>> get() = _searchedBooks
    val readType: LiveData<String> get() = _readType
    val myLibrarySearch: LiveData<String> get() = _myLibrarySearch
    val myLibrarySort: LiveData<String> get() = _myLibrarySort
    val getBooksCode: LiveData<Int> get() = _getBooksCode   //getBooks req 응답코드
    val searchFilterBtnClick: LiveData<Int> get() = _searchFilterBtnClick   //내서재에서 검색 필터 버튼이 눌렸는지 안눌렸는지 확인용 변수
    val sortFilterBtnClick: LiveData<Int> get() = _sortFilterBtnClick   //내서재에서 정렬 필터 버튼이 눌렸는지 안눌렸는지 확인용 변수

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

    fun setSearchFilterBtnClick(searchFilterBtnClick: Int) {
        _searchFilterBtnClick.value = searchFilterBtnClick
    }

    fun setSortFilterBtnClick(sortFilterBtnClick: Int) {
        _sortFilterBtnClick.value = sortFilterBtnClick
    }

    suspend fun requestBookList(category: String) {
        val req = withContext(Dispatchers.IO) {
            bookRepository.getBooks(category)
        }

        _getBooksCode.value = req["code"] as Int
        val books = req["books"] as BooksModel

        if (books != null) {
            when (category) {
                "NOW" -> {
                    _nowBooks.value = books.books
                    _nowBooks.value
                }
                "AFTER" -> {
                    _afterBooks.value = books.books
                    _afterBooks.value
                }
                "BEFORE" -> {
                    _beforeBooks.value = books.books
                    _beforeBooks.value
                }
            }
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
                val book = hashMap.await()!!["book"] as BookModel

                //읽는중, 완독일 땐 라이브데이터에 등록된 데이터 저장하기
                if (book.category!="BEFORE")
                    _book.value = book

                requestBookList(book.category)
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