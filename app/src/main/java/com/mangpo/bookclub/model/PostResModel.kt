package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class PostResModel(
    @SerializedName("data")
    val posts: ArrayList<PostModel> = ArrayList<PostModel>()
)
