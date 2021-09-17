package com.example.bookclub.model

import com.google.gson.annotations.SerializedName

data class BookResData(
    @SerializedName("data")
    var books: MutableList<BookModel>
)
