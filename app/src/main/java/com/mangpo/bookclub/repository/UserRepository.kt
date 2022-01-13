package com.mangpo.bookclub.repository

import android.util.Log
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.service.UserService
import java.lang.Exception
import java.net.UnknownHostException

class UserRepository(private val userService: UserService) {

    suspend fun login(user: UserModel): JsonObject {
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
        try {
            val res = userService.createUser(newUser)

            return if (res.isSuccessful) {
                Log.d("UserRepository", "회원가입 성공! -> ${res.body()}")
                res.body()!!.data
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
                else -> {
                    Log.e(
                        "UserRepository",
                        "getUser 실패! -> code: ${result.code()}, message: ${result.message()}}"
                    )

                    null
                }
            }
        } else {
            Log.e(
                "UserRepository",
                "getUser is not Successful! -> code: ${result.code()}, result: ${result}}"
            )

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

    suspend fun changePW(pwJsonObject: JsonObject): Int {
        val result = userService.changePW(pwJsonObject)

        return if (result.isSuccessful) {
            Log.d(
                "UserRepository", "changePW is Successful!\n" +
                        "code: ${result.code()}"
            )
            result.code()
        } else {
            Log.e(
                "UserRepository", "changePW is not Successful!\n" +
                        "password: $pwJsonObject\n" +
                        "message: ${result.message()}"
            )

            -1
        }
    }

    suspend fun quitMembership(userId: Long): Int {
        val result = userService.changeUserDormant(userId)

        if (result.isSuccessful) {
            Log.d(
                "UserRepository", "quitMembership is Successful!\n" +
                        "code: ${result.code()}"
            )
        } else {
            Log.e(
                "UserRepository", "changePW is not Successful!\n" +
                        "code: ${result.code()}\n" +
                        "message: ${result.message()}"
            )
        }

        return result.code()

    }

    suspend fun sendEmail() {
        userService.sendEmail()
    }

    suspend fun sendCode(code: Int): Int {
        val res = userService.sendCode(code)
        var result: Int = 0

        if (res.isSuccessful) {
            Log.d("UserRepository", "sendCode is Successful!\ncode: ${res.code()}")
            when (res.code()) {
                204 -> result = 1
                else -> Log.e(
                    "UserRepository",
                    "sendCode is not Successful!\ncode: ${res.code()}\nbody: ${res.body()}"
                )
            }
        } else {
            Log.e("UserRepository", "sendCode ERROR!\ncode: ${res.code()}\nerror: ${res.message()}")
        }

        return result
    }
}