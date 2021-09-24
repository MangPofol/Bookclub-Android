package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyLibraryViewModel() : ViewModel() {
    private val _mainFilter: MutableLiveData<Int> = MutableLiveData<Int>()
    private val _sortFilter: MutableLiveData<Int> = MutableLiveData<Int>()
    private val _libraryReadType: MutableLiveData<Int> = MutableLiveData<Int>()

    val mainFilter: LiveData<Int> get() = _mainFilter
    val sortFilter: LiveData<Int> get() = _sortFilter
    val libraryReadType: LiveData<Int> get() = _libraryReadType

    fun updateMainFilter(mainFilter: Int) {
        _mainFilter.value = mainFilter
    }

    fun updateSortFilter(sortFilter: Int) {
        _sortFilter.value = sortFilter
    }

    fun updateLibraryReadType(readType: Int) {
        _libraryReadType.value = readType
    }
}