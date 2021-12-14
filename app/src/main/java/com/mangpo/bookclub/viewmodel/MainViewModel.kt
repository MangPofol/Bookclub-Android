package com.mangpo.bookclub.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.util.AccountSharedPreference
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

    suspend fun login(user: JsonObject) {
        viewModelScope.launch {
            val loginResult = repository.login(user)

            if (loginResult.get("token")!=null)
                AccountSharedPreference.setJWT(loginResult.get("token").asString)

            _loginCode.value = loginResult.get("code").asInt
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