package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.ChangePasswordReq
import com.mangpo.bookclub.model.entities.LoginUser
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.entities.ValidateUser
import com.mangpo.bookclub.model.remote.*
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.UserService
import com.mangpo.bookclub.utils.ImgUtils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepositoryImpl: UserRepository {
    private val userService: UserService = ApiClient.userService

    override fun login(
        user: LoginUser,
        onResponse: (Response<LoginResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.login(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun validateDuplicate(
        email: ValidateUser,
        onResponse: (Response<ValidateDuplicateResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.validateEmail(email).enqueue(object: Callback<ValidateDuplicateResponse> {
            override fun onResponse(
                call: Call<ValidateDuplicateResponse>,
                response: Response<ValidateDuplicateResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<ValidateDuplicateResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun signUp(
        user: User,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.signUp(user).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun validateEmail(
        onResponse: (Response<String>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.validateEmail().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun validateEmailSendCode(
        emailCode: Int,
        onResponse: (Response<String>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.validateEmailSendCode(emailCode).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun getCurrentUserInfo(
        onResponse: (Response<UserDataResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.getCurrentUserInfo().enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(
                call: Call<UserDataResponse>,
                response: Response<UserDataResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun updateUser(
        user: User,
        userId: Int,
        onResponse: (Response<String>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.updateUser(user, userId).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun uploadImgFile(
        imgPath: String,
        onResponse: (Response<String>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val imgPart = ImgUtils.prepareFilePart("data", imgPath)
        userService.uploadImgFile(imgPart).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun uploadMultiImgFile(
        imgPaths: List<String>,
        onResponse: (Response<List<String>>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val imgParts: ArrayList<MultipartBody.Part> = arrayListOf()
        imgParts.add(ImgUtils.prepareFilePart("data", imgPaths[0]))

        userService.uploadMultiImgFile(imgParts).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun getTotalMemoCnt(
        onResponse: (Response<TotalCntResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.getTotalMemoCnt().enqueue(object : Callback<TotalCntResponse> {
            override fun onResponse(
                call: Call<TotalCntResponse>,
                response: Response<TotalCntResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<TotalCntResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun lostPassword(
        email: String,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.lostPassword(email).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun changePassword(
        password: ChangePasswordReq,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.changePassword(password).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun changeDormant(
        userId: Int,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.changeDormant(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun getTotalBookCnt(
        onResponse: (Response<TotalCntResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        userService.getTotalBookCnt().enqueue(object : Callback<TotalCntResponse> {
            override fun onResponse(
                call: Call<TotalCntResponse>,
                response: Response<TotalCntResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<TotalCntResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}