package com.mangpo.bookclub.model.remote

data class RecordsResponse (
    val data: List<RecordResponse>
)

data class RecordResponse (
    val postId: Int,
    val scope: String,
    val isIncomplete: Boolean,
    var title: String,
    var content: String,
    val createDate: String,
    val modifiedDate: String,
    var location: String,
    var readTime: String,
    var hyperlinkTitle: String,
    var hyperlink: String,
    var postImgLocations: List<String> = listOf(),
    val clubIdListForScope: List<Int> = listOf(),
    val likedList: List<Int> = listOf(),
    val commentsDto: List<String> = listOf()
)