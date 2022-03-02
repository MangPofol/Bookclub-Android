package com.mangpo.ourpage.model.entities

data class RecordRequest(
    var bookId: Int? = null,
    var scope: String? = null,
    var isIncomplete: Boolean = false,
    var location: String? = null,
    var readTime: String? = null,
    var hyperlinkTitle: String? = null,
    var hyperlink: String? = null,
    var title: String? = null,
    var content: String? = null,
    var postImgLocations: List<String> = listOf(),
    var clubIdListForScope: List<Int> = listOf()
)

data class RecordUpdateRequest(
    var scope: String? = null,
    var isIncomplete: Boolean = false,
    var location: String? = null,
    var readTime: String? = null,
    var hyperlinkTitle: String? = null,
    var hyperlink: String? = null,
    var title: String? = null,
    var content: String? = null,
    var postImgLocations: List<String> = listOf(),
    var clubIdListForScope: List<Int> = listOf()
)
