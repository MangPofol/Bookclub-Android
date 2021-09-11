package com.example.bookclub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclub.model.NaverBookModel
import com.example.bookclub.repository.BookRepository
import kotlinx.coroutines.launch

class SelectBookViewModel(): ViewModel() {
    private val bookRepository: BookRepository = BookRepository()

    private val _searchBookTitle: MutableLiveData<String> = MutableLiveData<String>()
    private val _books: MutableLiveData<MutableList<NaverBookModel>> = MutableLiveData<MutableList<NaverBookModel>>()

    val searchBookTitle : LiveData<String>
        get() = _searchBookTitle

    val books: LiveData<MutableList<NaverBookModel>>
        get() = _books

    init {
        _searchBookTitle.value = ""
    }

    suspend fun updateSearchBookTitle(title: String) {
        viewModelScope.launch {
            _searchBookTitle.value = title

            if (_searchBookTitle.value!="") {
                _books.value = bookRepository.getNaverBooksByTitle(_searchBookTitle.value.toString())
            }
        }
    }
}