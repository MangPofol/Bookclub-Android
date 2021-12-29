package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

//베타 버전 출시 후 사용
data class LikedModel(
    @SerializedName("userNickname")
    var nickname: String,

    @SerializedName("isLiked")
    var isLiked: Boolean
)
