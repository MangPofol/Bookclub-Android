package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _emailAlertVisibility = MutableLiveData<Int>()
    private val _logoutCode = MutableLiveData<Int>()
    private val _user = MutableLiveData<UserModel>()
    private val _updateUserCode = MutableLiveData<Int>()
    private val _quitMembershipCode = MutableLiveData<Int>()
    private val _sendCodeResult = MutableLiveData<Int>()    //이메인 인증 코드 전송 결과 코드

    val emailAlertVisibility: LiveData<Int> get() = _emailAlertVisibility
    val logoutCode: LiveData<Int> get() = _logoutCode
    val user: LiveData<UserModel> get() = _user
    val updateUserCode: LiveData<Int> get() = _updateUserCode
    val quitMembershipCode: LiveData<Int> get() = _quitMembershipCode
    val sendCodeResult: LiveData<Int> get() = _sendCodeResult   //이메인 인증 코드 전송 결과 코드

    suspend fun login(user: UserModel): String? {
        val token = viewModelScope.async {
            val loginResult = repository.login(user)
            val token = loginResult.get("token")

            if (token != null)
                loginResult.get("token").asString
            else null
        }

        return token.await()
    }

    suspend fun validateEmail(email: JsonObject) {
        val statusCode = withContext(viewModelScope.coroutineContext) {
            repository.validateEmail(email)
        }
        _emailAlertVisibility.value = statusCode
    }

    suspend fun createUser(user: UserModel) {
        viewModelScope.launch {
            val result = repository.createUser(user)

            if (result == null)
                _user.value = UserModel()
            else
                _user.value = result!!
        }
    }

    suspend fun getUser() {
        viewModelScope.launch {
            val user = repository.getUser()

            if (user != null)
                _user.value = user!!
            else
                _user.value = UserModel()
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

    suspend fun sendEmail() {
        viewModelScope.launch {
            repository.sendEmail()
        }
    }

    suspend fun sendCode(code: Int) {
        viewModelScope.launch {
            _sendCodeResult.value = repository.sendCode(code)
        }
    }

    suspend fun sendTempPWEmail(email: String) {
        viewModelScope.launch {
            repository.sendTempPWEmail(email)
        }
    }

    fun setUpdateUserCode(updateUserCode: Int) {
        _updateUserCode.value = updateUserCode
    }

    fun setUser(user: UserModel) {
        _user.value = user
    }
}