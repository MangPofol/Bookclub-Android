package com.mangpo.bookclub.service

import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/auth/login")
    suspend fun login(@Body user: JsonObject): Response<JsonObject>

    @POST("/users/validate-duplicate")
    suspend fun validateEmail(@Body email: JsonObject): Response<JsonObject>

    @POST("/users")
    suspend fun createUser(@Body newUser: UserModel): Response<JsonObject>

    @POST("/logout")
    suspend fun logout(): Response<String>
}