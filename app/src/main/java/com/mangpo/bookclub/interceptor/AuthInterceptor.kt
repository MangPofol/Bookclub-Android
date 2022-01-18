package com.mangpo.bookclub.interceptor

import com.mangpo.bookclub.util.JWTUtils
import com.mangpo.bookclub.view.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req =
            chain.request().newBuilder().addHeader("Authorization", "Bearer ${
                JWTUtils.getJWT(
                    MyApplication.instance
                )
            }").build()
        return chain.proceed(req)
    }
}