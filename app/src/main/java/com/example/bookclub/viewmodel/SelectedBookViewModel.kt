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

class SelectedBookViewModel(application: Application): AndroidViewModel(application) {
    private val kakaoBookRepository: KakaoBookRepository = KakaoBookRepository()
    private val bookRepository: BookRepository = BookRepository(application)
    private val _selectedBookReadType: MutableLiveData<Int> = MutableLiveData<Int>()
    private val _searchedBooks: MutableLiveData<MutableList<KakaoBookModel>> =
        MutableLiveData<MutableList<KakaoBookModel>>()   //검색을 통해 얻어낸 책 목록
    private val _selectedBook: MutableLiveData<BookModel> =
        MutableLiveData<BookModel>()  //사용자가 선택한 책
    private val _createdBook: MutableLiveData<BookModel> = MutableLiveData<BookModel>() //방금 추가된 책

    val selectedBookReadType: LiveData<Int> get() = _selectedBookReadType
    val searchedBooks: LiveData<MutableList<KakaoBookModel>> get() = _searchedBooks
    val selectedBook: LiveData<BookModel> get() = _selectedBook
    val createdBook: LiveData<BookModel> get() = _createdBook

    fun updateSelectedBookReadType(readType: Int) {
        _selectedBookReadType.value = readType
    }

    fun updateSelectedBook(position: Int) {
        Log.e("updateSelectedBook", _searchedBooks.value!![position].toString())

        val name = _searchedBooks.value!![position].title

        val splitIsbn = _searchedBooks.value!![position].isbn.split(" ")
        val isbn = if (splitIsbn[0] == "") {
            splitIsbn[0]
        } else {
            splitIsbn[1]
        }

        val image = _searchedBooks.value!![position].thumbnail

        _selectedBook.value = BookModel(name=name, isbn=isbn, image = image)
    }

    fun clearSelectedBook() {
        _selectedBook.value = BookModel()
    }

    suspend fun updateSearchBookTitle(title: String) {
        viewModelScope.launch {
            _searchedBooks.value =
                kakaoBookRepository.getKakaoBooks(title, "title", 20)!!.documents
        }
    }




}