package com.mangpo.bookclub.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: UserRepository): ViewModel() {

    private val _loginCode = MutableLiveData<Int>()
    private val _emailAlertVisibility = MutableLiveData<Int>()
    private val _logoutCode = MutableLiveData<Int>()

    private var _newUser = UserModel()
    private var _user: UserModel? = UserModel()

    val loginCode: LiveData<Int> get() = _loginCode
    val emailAlertVisibility: LiveData<Int> get() = _emailAlertVisibility
    val logoutCode: LiveData<Int> get() = _logoutCode

    /*fun getUser(user: HashMap<String, Any>): UserModel {
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

    suspend fun createUser() {
        _user = withContext(viewModelScope.coroutineContext) {
            repository.createUser(_newUser)
        }
    }

    suspend fun logout() {
        viewModelScope.launch {
            _logoutCode.value = repository.logout()
        }
    }

    fun updateNewUser(user: UserModel) {
        _newUser = user
    }

    fun getNewUser(): UserModel = _newUser

    fun getUser(): UserModel? = _user
}