package com.mangpo.bookclub.service

import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    /*@Headers("Content-Type: application/json")
    @POST("/users")
    fun createUser(@Body user: HashMap<String, Any>): Call<UserModel>*/

    @POST("/login")
    suspend fun login(@Body user: JsonObject): Response<String>

    @POST("/users/validate-duplicate")
    suspend fun validateEmail(@Body email: JsonObject): Response<JsonObject>

    @POST("/users")
    suspend fun createUser(@Body newUser: UserModel): Response<JsonObject>

    /*@POST("/logout")
    suspend fun logout(): String*/
}