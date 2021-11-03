package com.mangpo.bookclub.repository

import android.util.Log
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

        /*suspend fun logout(): String {
            return userService.logout()
        }*/
}