package com.mangpo.bookclub.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.UnknownHostException

class UserRepository(private val userService: UserService) {

    /*fun createUser(userReq: HashMap<String, Any>): UserModel {
        var userModel: UserModel = UserModel()

        userService.createUser(userReq).enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.e("회원가입 실패 ", t.toString())
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                Log.e("회원가입 성공",  response.body().toString())
                userModel = response.body()!!
            }
        })

        return userModel
    }*/

    suspend fun login(user: JsonObject): Int {
        return try {
            userService.login(user).code()
        } catch (e: UnknownHostException) {
            -1
        } catch (e: Exception) {
            e.printStackTrace()
            500
        }
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
                Log.d("UserRepository", "회원가입 실패! -> code: ${res.code()}, message: ${res.errorBody()}}")

                null
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "회원가입 코드 에러 발생! -> ${e.printStackTrace()}")

            return null
        }
    }

        /*suspend fun logout(): String {
            return userService.logout()
        }*/
}