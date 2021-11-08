package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class BooksModel(
    @SerializedName("data")
    var books: MutableList<BookModel>
)

data class BookModel(
    @SerializedName("id")
    var id: Long? = null,

    @SerializedName("name")
    var name: String = "",

    @SerializedName("isbn")
    var isbn: String = "",

    @SerializedName("category")
    var category: String = "",

    @SerializedName("createdDate")
    var createdDate: String = "",

    @SerializedName("modifiedDate")
    var modifiedDate: String = "",

    var imgPath: String? = null
)
