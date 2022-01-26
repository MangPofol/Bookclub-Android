package com.mangpo.bookclub.interceptor

import com.mangpo.bookclub.util.AuthUtils
import com.mangpo.bookclub.view.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = AuthUtils.getJWT(MyApplication.instance)

        var req =
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(req)
    }
}