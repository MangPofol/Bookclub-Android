package com.mangpo.ourpage.service

import com.mangpo.ourpage.model.entities.ChangePasswordReq
import com.mangpo.ourpage.model.entities.LoginUser
import com.mangpo.ourpage.model.entities.User
import com.mangpo.ourpage.model.entities.ValidateUser
import com.mangpo.ourpage.model.remote.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("/users/current")
    fun getCurrentUserInfo(): Call<UserDataResponse>
    @GET("/posts/total-count")
    fun getTotalMemoCnt(): Call<TotalCntResponse>
    @GET("/books/total-book")
    fun getTotalBookCnt(): Call<TotalCntResponse>

    @POST("/auth/login")
    fun login(@Body user: LoginUser): Call<LoginResponse>
    @POST("/users/validate-duplicate")
    fun validateEmail(@Body email: ValidateUser): Call<ValidateDuplicateResponse>
    @POST("/auth/signup")
    fun signUp(@Body user: User): Call<SignUpResponse>
    @POST("/users/validate-email")
    fun validateEmail(): Call<String>
    @POST("/users/validate-email-send-code")
    fun validateEmailSendCode(@Query("emailCode") emailCode: Int): Call<String>
    @Multipart
    @POST("/files/upload-multiple-files")
    fun uploadMultiImgFile(@Part data: List<MultipartBody.Part>): Call<List<String>>
    @POST("/users/lost-pw")
    fun lostPassword(@Query("userEmail") userEmail: String): Call<Void>
    @POST("/users/change-pw")
    fun changePassword(@Body password: ChangePasswordReq): Call<Void>
    @POST("/users/{userId}/change-dormant")
    fun changeDormant(@Path("userId") userId: Int): Call<Void>

    @PUT("/users/{userId}")
    fun updateUser(@Body user: User, @Path("userId") userId: Int): Call<String>
    @Multipart
    @PUT("/files/upload")
    fun uploadImgFile(@Part data: MultipartBody.Part): Call<String>
}