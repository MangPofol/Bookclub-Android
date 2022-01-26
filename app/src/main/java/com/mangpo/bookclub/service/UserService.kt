package com.mangpo.bookclub.service

import com.google.gson.JsonObject
import com.mangpo.bookclub.model.LoginResModel
import com.mangpo.bookclub.model.UserDataModel
import com.mangpo.bookclub.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("/users/current")
    suspend fun getUser(): Response<UserDataModel>

    @POST("/auth/login")
    suspend fun login(@Body user: UserModel): Response<LoginResModel>

    @POST("/users/validate-duplicate")
    suspend fun validateEmail(@Body email: JsonObject): Response<JsonObject>

    @POST("/auth/signup")
    suspend fun createUser(@Body newUser: UserModel): Response<UserDataModel>

    @POST("/logout")
    suspend fun logout(): Response<String>

    @POST("/users/change-pw")
    suspend fun changePW(@Body pwJsonObject: JsonObject): Response<String>

    @POST("/users/{userId}/change-dormant")
    suspend fun changeUserDormant(@Path("userId") userId: Long): Response<String>

    @POST("/users/validate-email")
    suspend fun sendEmail(): Response<String>

    @POST("/users/validate-email-send-code")
    suspend fun sendCode(@Query("emailCode") emailCode: Int): Response<JsonObject>

    @POST("/users/lost-pw")
    suspend fun sendTempPWEmail(@Query("userEmail") email: String): Response<String>

    @PUT("/users/{userId}")
    suspend fun updateUser(@Path("userId") userId: Long, @Body user: UserModel): Response<String>
}