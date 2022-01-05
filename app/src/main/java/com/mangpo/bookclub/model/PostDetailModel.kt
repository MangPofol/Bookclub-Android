package com.mangpo.bookclub.model

data class AllPostModel(
    var data: List<PostDetailModel>? = null
)

data class PostDetailModel(
    var book: BookModel? = null,
    var bookId: Long? = null,
    var postId: Long? = null,
    var scope: String = "",
    var isIncomplete: Boolean? = false,
    var title: String = "",
    var content: String = "",
    var createdDate: String = "",
    var modifiedDate: String = "",
    var location: String = "",
    var readTime: String = "",
    var hyperlinkTitle: String = "",
    var hyperlink: String = "",
    var postImgLocations: List<String> = listOf(),
    var postScopeClub: ClubModel? = null,
    var likedList: List<String> = listOf(),
    var commentsDto: List<String> = listOf()
)

/*
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
*/
