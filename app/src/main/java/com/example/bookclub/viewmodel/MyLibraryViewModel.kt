package com.example.bookclub.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyLibraryViewModel() : ViewModel() {
    val selectedFilter: MutableLiveData<Int> = MutableLiveData<Int>()
}