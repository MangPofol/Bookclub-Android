package com.mangpo.bookclub.model.remote

data class BookResponse(
    val data: List<Book>
)

data class Book(
    var id: Int? = null,
    val name: String = "",
    val isbn: String = "",
    var image: String = "",
    var category: String = "",
    val createdDate: String = "",
    val modifiedDate: String = ""
)
