package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("email")
    var email: String?,

    @SerializedName("nickname")
    var nickname: String?,

    @SerializedName("sex")
    var sex: String?,

    @SerializedName("birthdate")
    var birthdate: String?,

    @SerializedName("profileImgLocation")
    var profileImgLocation: String?
) {
    constructor(): this(null, null, null, null, null)
}