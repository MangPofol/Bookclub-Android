package com.example.bookclub.repository

import com.example.bookclub.BuildConfig
import com.example.bookclub.service.BookService
import com.example.bookclub.service.KakaoBookService
import com.example.bookclub.service.UserService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL
    private const val KAKAO_BOOK_API_BASE_URL = BuildConfig.KAKAO_BOOK_API_BASE_URL

    private val gson: Gson = GsonBuilder().setLenient().create()
    private val okHttpClient =
        OkHttpClient.Builder().cookieJar(JavaNetCookieJar(CookieManager())).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val kakaoRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(KAKAO_BOOK_API_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val userService: UserService = retrofit.create(UserService::class.java)
    val bookService: BookService = retrofit.create(BookService::class.java)
    val kakaoBookService: KakaoBookService = kakaoRetrofit.create(KakaoBookService::class.java)
}