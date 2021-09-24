package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class BookResData(
    @SerializedName("data")
    var books: MutableList<BookModel>
)
