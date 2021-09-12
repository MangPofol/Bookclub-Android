package com.example.bookclub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclub.model.NaverBookModel
import com.example.bookclub.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(): ViewModel() {
    private val bookRepository: BookRepository = BookRepository()

    private val _searchBookTitle: MutableLiveData<String> = MutableLiveData<String>()
    private val _selectedBookTitle: MutableLiveData<String> = MutableLiveData<String>()
    private val _books: MutableLiveData<MutableList<NaverBookModel>> = MutableLiveData<MutableList<NaverBookModel>>()

    private val searchBookTitle : LiveData<String>
        get() = _searchBookTitle

    val selectedBookTitle: LiveData<String>
        get() = _selectedBookTitle

    val books: LiveData<MutableList<NaverBookModel>>
        get() = _books

    suspend fun updateSearchBookTitle(title: String) {
        viewModelScope.launch {
            _searchBookTitle.value = title

            if (_searchBookTitle.value!="") {
                _books.value = bookRepository.getNaverBooksByTitle(_searchBookTitle.value.toString())
            }
        }
    }

    fun updateSelectedTookTitle(title: String) {
        _selectedBookTitle.value = title
    }
}