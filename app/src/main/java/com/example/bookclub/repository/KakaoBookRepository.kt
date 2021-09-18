package com.example.bookclub.repository

import com.example.bookclub.model.KakaoDocumentsModel
import com.example.bookclub.service.KakaoBookService

class KakaoBookRepository {
    private val kakaoBookService: KakaoBookService = ApiClient.kakaoBookService

    suspend fun getKakaoBooks(query: String, target: String, size: Int): KakaoDocumentsModel? {
        return kakaoBookService.getKakaoBooks(query, target, size).body()
    }
}