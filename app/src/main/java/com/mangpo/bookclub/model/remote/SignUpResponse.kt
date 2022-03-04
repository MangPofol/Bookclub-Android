package com.mangpo.bookclub.model.remote

data class SignUpResponse(
    val userId: Int,
    val email: String,
    val sex: String,
    val birthdate: String,
    val nickname: String,
    val introduce: String,
    val style: String,
    val goal: String,
    val profileImgLocation: String,
    val genres: List<String>,
    val isDormant: Boolean
)
