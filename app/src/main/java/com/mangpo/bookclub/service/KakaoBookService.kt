package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.KakaoDocumentsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoBookService {
    @Headers("Authorization: ${com.mangpo.bookclub.BuildConfig.KAKAOBOOK_KEY}")
    @GET("book")
    suspend fun getKakaoBooks(
        @Query("query") query: String,
        @Query("target") target: String,
        @Query("size") size: Int
    ): Response<KakaoDocumentsModel>
}