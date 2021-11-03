package com.mangpo.bookclub.repository

import android.util.Log
import com.google.gson.JsonObject
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        return userService.login(user).code()
    }

    /*suspend fun logout(): String {
        return userService.logout()
    }*/
}