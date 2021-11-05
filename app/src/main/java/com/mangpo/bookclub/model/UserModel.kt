package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class UserModel(
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("nickname")
    var nickname: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("sex")
    var sex: String? = null,

    @SerializedName("birthdate")
    var birthdate: String? = null,

    @SerializedName("introduce")
    var introduce: String? = null,

    @SerializedName("style")
    var style: String? = null,

    @SerializedName("goal")
    var goal: String? = null,

    @SerializedName("profileImgLocation")
    var profileImgLocation: String = "",

    @SerializedName("genres")
    var genres: List<String>? = null
)