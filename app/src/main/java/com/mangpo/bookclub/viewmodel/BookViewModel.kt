package com.mangpo.bookclub.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.KakaoBookModel
import com.mangpo.bookclub.repository.BookRepository
import com.mangpo.bookclub.repository.KakaoBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewModel(application: Application, private val bookRepository: BookRepository, private val kakaoBookRepository: KakaoBookRepository) : AndroidViewModel(application) {

    private val _searchedBooks: MutableLiveData<MutableList<KakaoBookModel>> =
        MutableLiveData<MutableList<KakaoBookModel>>()   //검색을 통해 얻어낸 책 목록
    private val _nowBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>()    //읽는 중 책 목록
    private val _beforeBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>() //읽고 싶은 책 목록
    private val _afterBooks: MutableLiveData<MutableList<BookModel>> =
        MutableLiveData<MutableList<BookModel>>()  //완독 책 목록
    private val _readType: MutableLiveData<String> = MutableLiveData<String>()
    private val _myLibrarySearch: MutableLiveData<String> = MutableLiveData<String>()
    private val _myLibrarySort: MutableLiveData<String> = MutableLiveData<String>()

    private var _selectedBook: BookModel? = null

//    private val _createdBook: MutableLiveData<BookModel?> = MutableLiveData<BookModel?>() //방금 추가된 책

//    private var books: MutableList<BookModel> = ArrayList()

//    val selectedBook: LiveData<BookModel> get() = _selectedBook
    val nowBooks: LiveData<MutableList<BookModel>> get() = _nowBooks
    val beforeBooks: LiveData<MutableList<BookModel>> get() = _beforeBooks
    val afterBooks: LiveData<MutableList<BookModel>> get() = _afterBooks
    val searchedBooks: LiveData<MutableList<KakaoBookModel>> get() = _searchedBooks
    val readType: LiveData<String> get() = _readType
    val myLibrarySearch: LiveData<String> get() = _myLibrarySearch
    val myLibrarySort: LiveData<String> get() = _myLibrarySort

    /*val selectedBookReadType: LiveData<Int> get() = _selectedBookReadType
    val createdBook: LiveData<BookModel?> get() = _createdBook*/

    suspend fun requestBookList(email: String, category: String) {
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

    fun getBookList(category: String): MutableList<BookModel>? {
        return when (category) {
            "NOW" -> _nowBooks.value
            "AFTER" -> _afterBooks.value
            else -> _beforeBooks.value
        }
    }

    fun getSelectedBook(): BookModel? = _selectedBook

    fun setSelectedBook(book: BookModel) {
        _selectedBook = book
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



    /*fun updateSelectedBookReadType(readType: Int) {
        _selectedBookReadType.value = readType
    }



    fun clearSelectedBook() {
        _selectedBook.value = BookModel()
    }*/

    /*suspend fun updateSearchBookTitle(title: String) {
        viewModelScope.launch {
            _searchedBooks.value =
                kakaoBookRepository.getKakaoBooks(title, "title", 20)!!.documents
        }
    }*/

    suspend fun getSearchedBooks(title: String) {
        viewModelScope.launch {
            _searchedBooks.value = kakaoBookRepository.getKakaoBooks(title, "title", 20)!!.documents
        }

    }

    fun clearSearchedBooks() {
        _searchedBooks.value!!.removeAll(_searchedBooks.value!!)
    }

    fun updateSearchedBooks(books: MutableList<KakaoBookModel>) {
        _searchedBooks.value = books
    }

    suspend fun createBook(book: BookModel): Int {
        try {
            book.isbn = book.isbn!!.split(" ")[0]
        } catch (e: Exception) {
            Log.e("BookViewModel-createBook", "책 추가하기에서 isbn split 오류 -> ${e.message}")
            book.isbn = book.isbn!!.split(" ")[1]
        }

        val newBook = withContext(viewModelScope.coroutineContext) {
            bookRepository.createBook(book)
        }

        /*when {
            newBook.code()==201 -> {
                Log.e("SelectedBookViewModel", "책 추가 성공! -> ${newBook.body()}")
                _nowBooks.value = getBooks("NOW")!!
                _createdBook.value = _nowBooks.value!!.filter {
                    it.isbn==newBook.body()!!.isbn
                }[0]
            }
            newBook.code()==400 -> {
                Log.e("SelectedBookViewModel", "책 추가 실패! -> 이미 등록된 책")
                _createdBook.value = null
            }
            else -> {
                Log.e("SelectedBookViewModel", "책 추가 실패! -> ${newBook.toString()}")
                _createdBook.value = null
            }
        }*/

        return newBook.code()
    }

    suspend fun createBeforeBook(newBook: BookModel): Int {
        try {
            newBook.isbn = newBook.isbn!!.split(" ")[0]
        } catch (e: Exception) {
            Log.e("BookViewModel-createBook", "책 추가하기에서 isbn split 오류 -> ${e.message}")
            newBook.isbn = newBook.isbn!!.split(" ")[1]
        }

        newBook.category = "BEFORE"

        val res = viewModelScope.async(Dispatchers.IO) {
            bookRepository.createBook(newBook)
        }

        when (res.await().code()) {
            201 -> {
                Log.e("BookViewModel", "읽고 싶은 책 추가 완료 -> ${res.await().body()}")

                val tempBook = _beforeBooks.value
                tempBook!!.add(res.await().body()!!)
                _beforeBooks.postValue(tempBook!!)
            }
        }

        return res.await().code()
    }
}