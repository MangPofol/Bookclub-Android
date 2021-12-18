package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class UserDataModel(
    val data: UserModel
)

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
    var introduce: String = "",

    @SerializedName("style")
    var style: String = "",

    @SerializedName("goal")
    var goal: String = "",

    @SerializedName("profileImgLocation")
    var profileImgLocation: String = "",

    @SerializedName("genres")
    var genres: List<String> = ArrayList<String>()
)