package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class UserDataModel(
    val data: UserModel
)

data class UserModel(
    @SerializedName("userId")
    val userId: Long? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("nickname")
    var nickname: String? = null,

    @SerializedName("password")
    var password: String? = null,

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

    @SerializedName("isDormant")
    var isDormant: Boolean = false,

    @SerializedName("genres")
    var genres: List<String> = ArrayList<String>()
)