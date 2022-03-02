package com.mangpo.ourpage.model.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("nickname")
    var nickname: String? = null,
    @SerializedName("password")
    var password: String? = null,
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
    var profileImgLocation: String? = null,
    @SerializedName("genres")
    var genres: List<String> = listOf()
)

data class LoginUser(
    var email: String = "",
    var password: String = ""
)

data class ValidateUser(
    var email: String = ""
)

data class ChangePasswordReq(
    var password: String
)
