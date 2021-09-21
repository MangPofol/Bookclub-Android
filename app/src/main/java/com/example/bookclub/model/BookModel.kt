package com.example.bookclub.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class BookModel(
    @SerializedName("id")
    var id: Long? = null,

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("isbn")
    var isbn: String? = "",

    @SerializedName("category")
    var category: String? = "",

    @SerializedName("createdDate")
    var createdDate: String? = "",

    @SerializedName("modifiedDate")
    var modifiedDate: String? = "",

    @SerializedName("likedList")
    var likedLists: List<LikedListModel>? = null,

    var image: String? = ""
)
