package com.mangpo.bookclub.service

import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserDataModel
import com.mangpo.bookclub.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("/users/current")
    suspend fun getUser(): Response<UserDataModel>

    @POST("/auth/login")
    suspend fun login(@Body user: JsonObject): Response<JsonObject>

    @POST("/users/validate-duplicate")
    suspend fun validateEmail(@Body email: JsonObject): Response<JsonObject>

    @POST("/auth/signup")
    suspend fun createUser(@Body newUser: UserModel): Response<JsonObject>

    @POST("/logout")
    suspend fun logout(): Response<String>

    @PUT("/users/{userId}")
    suspend fun updateUser(@Path("userId") userId: Long, @Body user: UserModel): Response<String>
}