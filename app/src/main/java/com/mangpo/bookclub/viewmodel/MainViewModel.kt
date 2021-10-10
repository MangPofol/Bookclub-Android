package com.mangpo.bookclub.viewmodel

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class MainViewModel(): ViewModel() {
    private val userRepository: UserRepository = UserRepository()

    fun getUser(user: HashMap<String, Any>): UserModel {
        return userRepository.createUser(user)
    }

    fun login(user: HashMap<String, String>) = viewModelScope.launch {
        val result = userRepository.login(user)
        Log.e("viewModel-로그인", result.toString())
    }

    fun logout() = viewModelScope.launch {
        val result = userRepository.logout()
        Log.e("viewModel-로그아웃", result.toString())
    }
}