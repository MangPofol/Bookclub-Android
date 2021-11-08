package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.KakaoDocumentsModel
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.KakaoBookService

class KakaoBookRepository(private val kakaoBookService: KakaoBookService) {

    suspend fun getKakaoBooks(query: String, target: String, size: Int): KakaoDocumentsModel? {
        return kakaoBookService.getKakaoBooks(query, target, size).body()
    }
}