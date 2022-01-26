package com.mangpo.bookclub.service

import com.mangpo.bookclub.interceptor.AuthInterceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager

object BaseService {
    private const val BASE_URL = com.mangpo.bookclub.BuildConfig.BASE_URL
    private const val KAKAO_BOOK_API_BASE_URL =
        com.mangpo.bookclub.BuildConfig.KAKAO_BOOK_API_BASE_URL

    private val okHttpClient =
        OkHttpClient.Builder().cookieJar(JavaNetCookieJar(CookieManager()))
            .addInterceptor(AuthInterceptor()).build()

    fun getClient(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getKakaoClient(): Retrofit = Retrofit.Builder()
        .baseUrl(KAKAO_BOOK_API_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}