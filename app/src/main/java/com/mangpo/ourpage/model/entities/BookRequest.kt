package com.mangpo.ourpage.model.entities

data class BookRequest(
    var name: String,
    var isbn: String,
    var category: String
)

data class BookCategoryRequest(
    var category: String
)
