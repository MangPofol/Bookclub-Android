package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class UserModel(
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("nickname")
    val nickname: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("sex")
    val sex: String? = null,

    @SerializedName("birthdate")
    val birthdate: LocalDateTime? = null,

    @SerializedName("introduce")
    val introduce: String? = null,

    @SerializedName("style")
    val style: String? = null,

    @SerializedName("goal")
    val goal: String? = null,

    @SerializedName("profileImgLocation")
    val profileImgLocation: String? = null,

    @SerializedName("genres")
    val genres: List<String>? = null
)