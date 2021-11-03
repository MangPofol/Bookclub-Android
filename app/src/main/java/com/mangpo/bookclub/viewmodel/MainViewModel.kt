package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mangpo.bookclub.repository.UserRepository
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: UserRepository): ViewModel() {
    /*private val userRepository: UserRepository = UserRepository()

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
    }*/

    suspend fun login(user: JsonObject): Int =
        withContext(viewModelScope.coroutineContext) {
            repository.login(user)
        }
}