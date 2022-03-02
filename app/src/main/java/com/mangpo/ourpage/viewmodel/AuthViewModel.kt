package com.mangpo.ourpage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mangpo.ourpage.model.entities.ChangePasswordReq
import com.mangpo.ourpage.model.entities.LoginUser
import com.mangpo.ourpage.model.entities.User
import com.mangpo.ourpage.model.entities.ValidateUser
import com.mangpo.ourpage.repository.UserRepositoryImpl
import com.mangpo.ourpage.utils.AuthUtils

class AuthViewModel : ViewModel() {
    private val userRepositoryImpl: UserRepositoryImpl = UserRepositoryImpl()

    private val _loginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _validateCode: MutableLiveData<Int> = MutableLiveData()
    val validateCode: LiveData<Int> get() = _validateCode

    private val _signUpCode: MutableLiveData<Int> = MutableLiveData()
    val signUpCode: LiveData<Int> get() = _signUpCode

    private val _validateEmailCode: MutableLiveData<Int> = MutableLiveData()
    val validateEmailCode: LiveData<Int> get() = _validateEmailCode

    private val _validateEmailSendCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val validateEmailSendCode: LiveData<Event<Int>> get() = _validateEmailSendCode

    private val _lostPasswordCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val lostPasswordCode: LiveData<Event<Int>> get() = _lostPasswordCode

    private val _changePasswordCode: MutableLiveData<Int> = MutableLiveData()
    val changePasswordCode: LiveData<Int> get() = _changePasswordCode

    private val _changeDormantCode: MutableLiveData<Int> = MutableLiveData()
    val changeDormantCode: LiveData<Int> get() = _changeDormantCode

    fun login(user: LoginUser) {
        userRepositoryImpl.login(
            user = user,
            onResponse = {
                Log.d("AuthViewModel", "login Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==200) {
                    AuthUtils.setJWT(it.body()!!.token)
                    AuthUtils.setEmail(user.email)
                    AuthUtils.setPassword(user.password)
                    _loginSuccess.value = true
                } else
                    _loginSuccess.value = false

            },
            onFailure = {
                Log.e("AuthViewModel", "login Fail!\nmessage: ${it.message}")
                _loginSuccess.value = false
            })
    }

    fun validateDuplicate(email: String) {
        val validateUser: ValidateUser = ValidateUser(email)

        userRepositoryImpl.validateDuplicate(
            email = validateUser,
            onResponse = {
                Log.d("AuthViewModel", "validateDuplicate Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _validateCode.value = it.code()
            },
            onFailure = {
                Log.e("AuthViewModel", "validateDuplicate Fail!\nmessage: ${it.message}")
                _validateCode.value = 600
            })
    }

    fun signUp(user: User) {
        userRepositoryImpl.signUp(
            user = user,
            onResponse = {
                Log.d("AuthViewModel", "signUp Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _signUpCode.value = it.code()
            },
            onFailure = {
                Log.e("AuthViewModel", "signUp Fail!\nmessage: ${it.message}")
                _signUpCode.value = 600
            }
        )
    }

    fun validateEmail() {
        userRepositoryImpl.validateEmail(
            onResponse = {
                Log.d("AuthViewModel", "validateEmail Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _validateEmailCode.value = it.code()
            },
            onFailure = {
                Log.e("AuthViewModel", "validateEmail Fail!\nmessage: ${it.message}")
                _validateEmailCode.value = 600
            }
        )
    }

    fun validateEmailSendCode(emailCode: Int) {
        userRepositoryImpl.validateEmailSendCode(
            emailCode = emailCode,
            onResponse = {
                Log.d("AuthViewModel", "validateEmailSendCode Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _validateEmailSendCode.value = Event(it.code())
            },
            onFailure = {
                Log.e("AuthViewModel", "validateEmailSendCode Fail!\nmessage: ${it.message}")
                _validateEmailSendCode.value = Event(600)
            }
        )
    }

    fun lostPassword(email: String) {
        userRepositoryImpl.lostPassword(
            email = email,
            onResponse = {
                Log.d("AuthViewModel", "lostPassword Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _lostPasswordCode.value= Event(it.code())
            },
            onFailure = {
                Log.e("AuthViewModel", "lostPassword Fail!\nmessage: ${it.message}")
                _lostPasswordCode.value = Event(600)
            }
        )
    }

    fun changePassword(password: ChangePasswordReq) {
        userRepositoryImpl.changePassword(
            password = password,
            onResponse = {
                Log.d("AuthViewModel", "changePassword Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _changePasswordCode.value = it.code()

                if (it.code()==204)
                    AuthUtils.setPassword(password.password)
            },
            onFailure = {
                Log.e("AuthViewModel", "changePassword Fail!\nmessage: ${it.message}")
                _changePasswordCode.value = 600
            }
        )
    }

    fun changeDormant(userId: Int) {
        userRepositoryImpl.changeDormant(
            userId = userId,
            onResponse = {
                Log.d("AuthViewModel", "changeDormant Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _changeDormantCode.value = it.code()
            },
            onFailure = {
                Log.e("AuthViewModel", "changeDormant Fail!\nmessage: ${it.message}")
                _changeDormantCode.value = 600
            }
        )
    }
}