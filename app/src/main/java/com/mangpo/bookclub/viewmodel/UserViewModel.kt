package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.remote.UserResponse
import com.mangpo.bookclub.repository.UserRepositoryImpl

class UserViewModel: BaseViewModel() {
    private val userRepositoryImpl: UserRepositoryImpl = UserRepositoryImpl()

    private val _getUserCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val getUserCode: LiveData<Event<Int>> get() = _getUserCode
    private var user: UserResponse? = null

    private val _updateUserCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val updateUserCode: LiveData<Event<Int>> get() = _updateUserCode

    private val _uploadImgFileCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val uploadImgFileCode: LiveData<Event<Int>> get() = _uploadImgFileCode
    private var imgPath: String? = null

    private val _totalMemoCnt: MutableLiveData<Int> = MutableLiveData()
    val totalMemoCnt: LiveData<Int> get() = _totalMemoCnt

    private val _totalBookCnt: MutableLiveData<Int> = MutableLiveData()
    val totalBookCnt: LiveData<Int> get() = _totalBookCnt

    fun getUserInfo() {
        userRepositoryImpl.getCurrentUserInfo(
            onResponse = {
                user = if (it.code()==200)
                    it.body()!!.data
                else
                    null

                _getUserCode.value = Event(it.code())
            },
            onFailure = {
                user = null
            }
        )
    }

    fun getUser(): UserResponse? = user

    fun updateUser(user: User, userId: Int) {
        userRepositoryImpl.updateUser(
            user = user,
            userId = userId,
            onResponse = {
                _updateUserCode.value = Event(it.code())
            },
            onFailure = {
                _updateUserCode.value = Event(600)
            }
        )
    }

    fun uploadImgFile(imgPath: String) {
        userRepositoryImpl.uploadMultiImgFile(
            imgPaths = listOf(imgPath),
            onResponse = {
                if (it.code()==200)
                    this.imgPath = it.body()!![0]

                _uploadImgFileCode.value = Event(it.code())
            },
            onFailure = {
                _uploadImgFileCode.value = Event(600)
            }
        )
    }

    fun getImgPath(): String? = imgPath

    fun getTotalMemoCnt() {
        userRepositoryImpl.getTotalMemoCnt(
            onResponse = {
                if (it.code()==200)
                    _totalMemoCnt.value = it.body()!!.data
                else
                    _totalMemoCnt.value = -1
            },
            onFailure = {
                _totalMemoCnt.value = -1
            }
        )
    }

    fun getTotalBookCnt() {
        userRepositoryImpl.getTotalBookCnt(
            onResponse = {
                if (it.code()==200)
                    _totalBookCnt.value = it.body()!!.data
                else
                    _totalBookCnt.value = -1
            },
            onFailure = {
                _totalBookCnt.value = -1
            }
        )
    }
}