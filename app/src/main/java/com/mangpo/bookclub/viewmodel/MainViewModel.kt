package com.mangpo.bookclub.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.util.AuthUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: UserRepository, private val context: Context) :
    ViewModel() {
    private val _emailAlertVisibility = MutableLiveData<Int>()
    private val _logoutCode = MutableLiveData<Int>()
    private val _user = MutableLiveData<UserModel>()
    private val _updateUserCode = MutableLiveData<Int>()
    private val _quitMembershipCode = MutableLiveData<Int>()
    private val _sendCodeResult = MutableLiveData<Int>()    //이메인 인증 코드 전송 결과 코드
    private val _loginCode = MutableLiveData<Int>() //로그인 결과 코드

    val emailAlertVisibility: LiveData<Int> get() = _emailAlertVisibility
    val logoutCode: LiveData<Int> get() = _logoutCode
    val user: LiveData<UserModel> get() = _user
    val updateUserCode: LiveData<Int> get() = _updateUserCode
    val quitMembershipCode: LiveData<Int> get() = _quitMembershipCode
    val sendCodeResult: LiveData<Int> get() = _sendCodeResult   //이메인 인증 코드 전송 결과 코드
    val loginCode: LiveData<Int> get() = _loginCode //로그인 결과 코드

    suspend fun login(user: UserModel) {
        viewModelScope.launch {
            val loginResModel = repository.login(user)

            if (loginResModel.code == 200) {
                AuthUtils.setJWT(context, loginResModel.token!!)
                AuthUtils.setEmail(context, user.email!!)
                AuthUtils.setPassword(context, user.password!!)
            }

            _loginCode.value = loginResModel.code
        }
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