package com.mangpo.bookclub.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager

object ApiClient {
    private const val BASE_URL = com.mangpo.bookclub.BuildConfig.BASE_URL
    private const val KAKAO_BOOK_API_BASE_URL =
        com.mangpo.bookclub.BuildConfig.KAKAO_BOOK_API_BASE_URL

    private val gson: Gson = GsonBuilder().setLenient().create()
    private val okHttpClient =
        OkHttpClient.Builder().cookieJar(JavaNetCookieJar(CookieManager())).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val userService: UserService by lazy {
        BaseService.getClient(BASE_URL).create(UserService::class.java)
    }
    val bookService: BookService by lazy {
        BaseService.getClient(BASE_URL).create(BookService::class.java)
    }
    val kakaoBookService: KakaoBookService by lazy {
        BaseService.getKakaoClient(KAKAO_BOOK_API_BASE_URL).create(KakaoBookService::class.java)
    }
    val postService: PostService by lazy {
        BaseService.getClient(BASE_URL).create(PostService::class.java)
    }

    val clubService: ClubService = retrofit.create(ClubService::class.java)

}