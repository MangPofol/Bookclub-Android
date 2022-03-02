package com.mangpo.ourpage.config

import com.mangpo.ourpage.utils.AuthUtils
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val jwtToken: String = AuthUtils.getJWT()
        val req = chain.request().newBuilder().addHeader("Authorization", "Bearer $jwtToken").build()

        return chain.proceed(req)
    }
}