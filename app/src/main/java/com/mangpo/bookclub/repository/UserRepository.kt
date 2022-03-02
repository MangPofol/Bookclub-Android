package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.ChangePasswordReq
import com.mangpo.bookclub.model.entities.LoginUser
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.entities.ValidateUser
import com.mangpo.bookclub.model.remote.*
import retrofit2.Response

interface UserRepository {
    fun login(user: LoginUser, onResponse: (Response<LoginResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun validateDuplicate(email: ValidateUser, onResponse: (Response<ValidateDuplicateResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun signUp(user: User, onResponse: (Response<SignUpResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun validateEmail(onResponse: (Response<String>) -> Unit, onFailure: (Throwable) -> Unit)
    fun validateEmailSendCode(emailCode: Int, onResponse: (Response<String>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getCurrentUserInfo(onResponse: (Response<UserDataResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun updateUser(user: User, userId: Int, onResponse: (Response<String>) -> Unit, onFailure: (Throwable) -> Unit)
    fun uploadImgFile(imgPath: String, onResponse: (Response<String>) -> Unit, onFailure: (Throwable) -> Unit)
    fun uploadMultiImgFile(imgPaths: List<String>, onResponse: (Response<List<String>>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getTotalMemoCnt(onResponse: (Response<TotalCntResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun lostPassword(email: String, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun changePassword(password: ChangePasswordReq, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun changeDormant(userId: Int, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getTotalBookCnt(onResponse: (Response<TotalCntResponse>) -> Unit, onFailure: (Throwable) -> Unit)
}