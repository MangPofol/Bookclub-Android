package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mangpo.bookclub.model.entities.ChangePasswordReq
import com.mangpo.bookclub.model.entities.LoginUser
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.entities.ValidateUser
import com.mangpo.bookclub.repository.UserRepositoryImpl
import com.mangpo.bookclub.utils.AuthUtils

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
                if (it.code()==200) {
                    AuthUtils.setJWT(it.body()!!.token)
                    AuthUtils.setEmail(user.email)
                    AuthUtils.setPassword(user.password)
                    _loginSuccess.value = true
                } else
                    _loginSuccess.value = false

            },
            onFailure = {
                _loginSuccess.value = false
            })
    }

    fun validateDuplicate(email: String) {
        val validateUser: ValidateUser = ValidateUser(email)

        userRepositoryImpl.validateDuplicate(
            email = validateUser,
            onResponse = {
                _validateCode.value = it.code()
            },
            onFailure = {
                _validateCode.value = 600
            })
    }

    fun signUp(user: User) {
        userRepositoryImpl.signUp(
            user = user,
            onResponse = {
                _signUpCode.value = it.code()
            },
            onFailure = {
                _signUpCode.value = 600
            }
        )
    }

    fun validateEmail() {
        userRepositoryImpl.validateEmail(
            onResponse = {
                _validateEmailCode.value = it.code()
            },
            onFailure = {
                _validateEmailCode.value = 600
            }
        )
    }

    fun validateEmailSendCode(emailCode: Int) {
        userRepositoryImpl.validateEmailSendCode(
            emailCode = emailCode,
            onResponse = {
                _validateEmailSendCode.value = Event(it.code())
            },
            onFailure = {
                _validateEmailSendCode.value = Event(600)
            }
        )
    }

    fun lostPassword(email: String) {
        userRepositoryImpl.lostPassword(
            email = email,
            onResponse = {
                _lostPasswordCode.value= Event(it.code())
            },
            onFailure = {
                _lostPasswordCode.value = Event(600)
            }
        )
    }

    fun changePassword(password: ChangePasswordReq) {
        userRepositoryImpl.changePassword(
            password = password,
            onResponse = {
                _changePasswordCode.value = it.code()

                if (it.code()==204)
                    AuthUtils.setPassword(password.password)
            },
            onFailure = {
                _changePasswordCode.value = 600
            }
        )
    }

    fun changeDormant(userId: Int) {
        userRepositoryImpl.changeDormant(
            userId = userId,
            onResponse = {
                _changeDormantCode.value = it.code()
            },
            onFailure = {
                _changeDormantCode.value = 600
            }
        )
    }
}