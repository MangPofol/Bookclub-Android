package com.example.bookclub.model

import com.google.gson.annotations.SerializedName

data class LikedListModel(
    @SerializedName("userNickname")
    var nickname: String,

    @SerializedName("isLiked")
    var isLiked: Boolean
)
