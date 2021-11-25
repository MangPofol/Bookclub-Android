package com.mangpo.bookclub.model

data class PostModel(
    var bookId: Long? = null,
    var scope: String? = null,
    var isIncomplete: Boolean = false,
    var location: String? = null,
    var readTime: String? = null,
    var hyperlinkTitle: String? = null,
    var hyperlink: String? = null,
    var title: String? = null,
    var content: String? = null,
    var postImgLocations: List<String> = ArrayList<String>(),
//    var clubIdListForScope: List<Long> = ArrayList<Long>()
)