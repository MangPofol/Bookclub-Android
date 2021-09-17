package com.example.bookclub.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class BookModel(
    @SerializedName("id")
    var id: Long? = null,

    @SerializedName("name")
    var name: String,

    @SerializedName("isbn")
    var isbn: String? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("createdDate")
    var createdDate: String? = null,

    @SerializedName("modifiedDate")
    var modifiedDate: String? = null,

    @SerializedName("likedList")
    var likedLists: List<LikedListModel>? = null,

    var image: String? = null
)
