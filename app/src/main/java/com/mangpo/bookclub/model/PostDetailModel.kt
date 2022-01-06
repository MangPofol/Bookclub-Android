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