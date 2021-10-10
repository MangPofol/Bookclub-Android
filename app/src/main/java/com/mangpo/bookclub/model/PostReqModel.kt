package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class PostReqModel (
    var bookId: Long? = null,
    var type: String = "",
    var scope: String = "",
    val isIncomplete: Boolean = false,
    var imgLocation: MutableList<String> = ArrayList<String>(),
    var title: String = "",
    var content: String = "",
    val clubIdListForScope: MutableList<Long> = ArrayList<Long>()
)