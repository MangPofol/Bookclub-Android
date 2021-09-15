package com.example.bookclub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclub.model.BookModel
import com.example.bookclub.model.NaverBookModel
import com.example.bookclub.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class BookViewModel(): ViewModel() {
    private val bookRepository: BookRepository = BookRepository()

//    private val _searchBookTitle: MutableLiveData<String> = MutableLiveData<String>()
    private val _selectedBook: MutableLiveData<NaverBookModel> = MutableLiveData<NaverBookModel>()  //사용자가 선택한 책
    private val _searchedBooks: MutableLiveData<MutableList<NaverBookModel>> = MutableLiveData<MutableList<NaverBookModel>>()   //검색을 통해 얻어낸 책 목록
    private val _nowBooks: MutableLiveData<MutableList<NaverBookModel>> = MutableLiveData<MutableList<NaverBookModel>>()    //읽는 중 책 목록
    private val _beforeBooks: MutableLiveData<MutableList<NaverBookModel>> = MutableLiveData<MutableList<NaverBookModel>>() //읽고 싶은 책 목록
    private val _afterBooks: MutableLiveData<MutableList<NaverBookModel>> = MutableLiveData<MutableList<NaverBookModel>>()  //완독 책 목록

//    private val searchBookTitle : LiveData<String> get() = _searchBookTitle
    private val selectedBook: LiveData<NaverBookModel> get() = _selectedBook
    val searchedBooks: LiveData<MutableList<NaverBookModel>> get() = _searchedBooks
    val nowBooks: LiveData<MutableList<NaverBookModel>> get() = _nowBooks
    val beforeBooks: LiveData<MutableList<NaverBookModel>> get() = _beforeBooks
    val afterBooks: LiveData<MutableList<NaverBookModel>> get() = _afterBooks

    suspend fun updateSearchBookTitle(title: String) {
        viewModelScope.launch {
            if (title!="")
                _searchedBooks.value = bookRepository.getNaverBooksByTitle(title)
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
        val book: BookModel = BookModel(
            name=_selectedBook.value!!.title,
            isbn = _selectedBook.value!!.isbn,
            category = readType
        )

        val code = withContext(Dispatchers.IO) {
            //서버에 책 추가 요청 보냄
            val code = bookRepository.createBook(book).code()

            if (code==201) {    //책이 서버 DB에 성공적으로 저장되면 Room 에 데이터 저장

            }

            return@withContext code
        }

        return code
    }
}