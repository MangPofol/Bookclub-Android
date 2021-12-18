package com.mangpo.bookclub.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.service.UserService
import java.lang.Exception
import java.net.UnknownHostException

class UserRepository(private val userService: UserService) {

    suspend fun login(user: JsonObject): JsonObject {
        var jsonObject: JsonObject = JsonObject()

        try {
            val result = userService.login(user)
            jsonObject = result.body()!!
            jsonObject.addProperty("code", result.code())
        } catch (e: UnknownHostException) {
            jsonObject.addProperty("code", -1)
        } catch (e: Exception) {
            e.printStackTrace()
            jsonObject.addProperty("code", 500)
        }

        return jsonObject
    }

    suspend fun validateEmail(email: JsonObject): Int = userService.validateEmail(email).code()

    suspend fun createUser(newUser: UserModel): UserModel? {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()

        try {
            val res = userService.createUser(newUser)

            return if (res.isSuccessful) {
                Log.d("UserRepository", "회원가입 성공! -> ${res.body()}")

                gson.fromJson(res.body()!!.get("data"), UserModel::class.java)
            } else {
                Log.d(
                    "UserRepository",
                    "회원가입 실패! -> code: ${res.code()}, message: ${res.errorBody()}}"
                )

                null
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "회원가입 코드 에러 발생! -> ${e.printStackTrace()}")

            return null
        }
    }

    suspend fun getUser(): UserModel? {
        val result = userService.getUser()

        return if (result.isSuccessful) {
            when (result.code()) {
                200 -> result.body()!!.data
                else -> null
            }
        } else {
            null
        }
    }

    suspend fun logout(): Int {
        return try {
            userService.logout().code()
        } catch (e: Exception) {
            e.printStackTrace()
            500
        }
    }

    suspend fun updateUser(user: UserModel): Int {
        val result = userService.updateUser(user.userId!!, user)

        if (result.isSuccessful) {
            when (result.code()) {
                204 -> {
                    Log.d("UserRepository", "updateUser is Successful!")
                }
                else -> {
                    Log.e(
                        "UserRepository", "updateUser ERROR!\n" +
                                "code: ${result.code()}\n" +
                                "requestBody: $user"
                    )
                }
            }
        } else {
            Log.e(
                "UserRepository", "updateUser is not Successful!\n" +
                        "code: ${result.code()}\n" +
                        "requestBody: $user\n" +
                        "message: ${result.message()}"
            )
        }

        return result.code()
    }
}