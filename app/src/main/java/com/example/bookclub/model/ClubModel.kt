package com.example.bookclub.model

data class ClubModel (
    var id: Long? = null,
    var name: String = "",
    var colorSet: String = "",
    var level: Int = 1,
    var presidentId: Long? = null,
    var description: String = "",
    var createdDate: String = "",
    var modifiedDate: String = ""
)