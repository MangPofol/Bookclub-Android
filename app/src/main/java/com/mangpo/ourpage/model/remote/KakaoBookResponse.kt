package com.mangpo.ourpage.model.remote

data class KakaoBookResponse(
    var documents: MutableList<KakaoBook>
)

data class KakaoBook(
    val isbn: String,
    val thumbnail: String,
    val title: String
)
