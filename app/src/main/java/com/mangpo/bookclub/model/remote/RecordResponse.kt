package com.mangpo.bookclub.model.remote

import com.mangpo.bookclub.model.entities.Link

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
    var linkResponseDtos: List<Link> = listOf(),
    var postImgLocations: List<String> = listOf(),
    val clubIdListForScope: List<Int> = listOf(),
    val likedList: List<Int> = listOf(),
    val commentsDto: List<String> = listOf()
)