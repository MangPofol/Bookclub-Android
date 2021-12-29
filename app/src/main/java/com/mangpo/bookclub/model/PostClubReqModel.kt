package com.mangpo.bookclub.model

//베타 버전 출시 후 사용
data class PostClubReqModel(
    var bookId: Long? = null,
    var type: String = "",
    var scope: String = "",
    val isIncomplete: Boolean = false,
    var imgLocation: MutableList<String> = ArrayList<String>(),
    var title: String = "",
    var content: String = "",
    val clubIdListForScope: MutableList<Long> = ArrayList<Long>()
)
