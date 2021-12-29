package com.mangpo.bookclub.model

data class AllPostModel(
    var data: List<PostDetailModel>? = null
)

data class PostDetailModel(
    var postId: Long,
    var scope: String,
    var isIncomplete: Boolean,
    var title: String,
    var content: String,
    var createdDate: String,
    var modifiedDate: String,
    var location: String?,
    var readTime: String?,
    var hyperlinkTitle: String?,
    var hyperlink: String?,
    var postImgLocations: List<String> = ArrayList<String>(),
    var postScopeClub: ClubModel? = null,
    var likedList: List<String> = ArrayList<String>(),
    var commentsDto: List<String> = ArrayList<String>()
)
