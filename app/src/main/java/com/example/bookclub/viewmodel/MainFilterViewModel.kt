package com.example.bookclub.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainFilterViewModel : ViewModel() {
    val selectedFilter: MutableLiveData<Int> = MutableLiveData<Int>()
}