package com.mangpo.bookclub.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.util.AccountSharedPreference
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginCode = MutableLiveData<Int>()
    private val _emailAlertVisibility = MutableLiveData<Int>()
    private val _logoutCode = MutableLiveData<Int>()
    private val _user = MutableLiveData<UserModel>()
    private val _updateUserCode = MutableLiveData<Int>()
    private val _quitMembershipCode = MutableLiveData<Int>()

    private var _newUser = UserModel()

    val loginCode: LiveData<Int> get() = _loginCode
    val emailAlertVisibility: LiveData<Int> get() = _emailAlertVisibility
    val logoutCode: LiveData<Int> get() = _logoutCode
    val user: LiveData<UserModel> get() = _user
    val updateUserCode: LiveData<Int> get() = _updateUserCode
    val quitMembershipCode: LiveData<Int> get() = _quitMembershipCode

    suspend fun login(user: JsonObject) {
        viewModelScope.launch {
            val loginResult = repository.login(user)

            if (loginResult.get("token") != null)
                AccountSharedPreference.setJWT(loginResult.get("token").asString)

            _loginCode.value = loginResult.get("code").asInt
        }
    }

    suspend fun validateEmail(email: JsonObject) {
        val statusCode = withContext(viewModelScope.coroutineContext) {
            repository.validateEmail(email)
        }
        if (statusCode == 204)
            _emailAlertVisibility.value = View.INVISIBLE
        else
            _emailAlertVisibility.value = View.VISIBLE
    }

    suspend fun createUser() {
        _newUser = withContext(viewModelScope.coroutineContext) {
            repository.createUser(_newUser)
        }!!
    }

    suspend fun getUser() {
        viewModelScope.launch {
            _user.value = repository.getUser()!!
        }
    }

    suspend fun logout() {
        viewModelScope.launch {
            _logoutCode.value = repository.logout()
        }
    }

    suspend fun updateUser(user: UserModel) {
        viewModelScope.launch {
            _updateUserCode.value = repository.updateUser(user)

            if (_updateUserCode.value == 204)
                _user.value = user
        }
    }

    suspend fun changePW(password: String) {
        val pwJsonObject = JsonObject()
        pwJsonObject.addProperty("password", password)

        viewModelScope.launch {
            _updateUserCode.value = repository.changePW(pwJsonObject)
        }
    }

    suspend fun quitMembership(userId: Long) {
        viewModelScope.launch {
            _quitMembershipCode.value = repository.quitMembership(userId)
        }
    }

    fun updateNewUser(user: UserModel) {
        _newUser = user
    }

    fun getNewUser(): UserModel = _newUser

}