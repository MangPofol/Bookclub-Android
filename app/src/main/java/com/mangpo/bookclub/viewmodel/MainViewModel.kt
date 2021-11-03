package com.mangpo.bookclub.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mangpo.bookclub.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: UserRepository): ViewModel() {

    private val _loginCode = MutableLiveData<Int>()
    private val _emailAlertVisibility = MutableLiveData<Int>()

    val loginCode: LiveData<Int> get() = _loginCode
    val emailAlertVisibility: LiveData<Int> get() = _emailAlertVisibility

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

    suspend fun login(user: JsonObject) {
        viewModelScope.launch {
            _loginCode.value = repository.login(user)
        }
    }


    suspend fun validateEmail(email: JsonObject) {
        val statusCode = withContext(viewModelScope.coroutineContext) {
            repository.validateEmail(email)
        }

        if (statusCode==204)
            _emailAlertVisibility.value = View.INVISIBLE
        else
            _emailAlertVisibility.value = View.VISIBLE
    }

}