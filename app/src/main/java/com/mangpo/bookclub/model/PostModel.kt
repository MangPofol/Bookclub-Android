package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

/*"id": 9,
"type": "MEMO",
"scope": "PUBLIC",
"isIncomplete": true,
"imgLocation": "123somewhere321",
"title": "죽과를 읽고",
"content": "다시 생각하니 노잼이였다",
"createdDate": "2021-08-19T16:53:11",
"modifiedDate": "2021-08-20T16:43:43",
"likedList": [
{
    "userNickname": "rabbit",
    "isLiked": true
}
],
"commentsDto": [
{
    "userNickname": "rabbit",
    "content": "this is content2.",
    "createdDate": "2021-08-31T15:37:21",
    "modifiedDate": "2021-08-31T15:37:21"
},
{
    "userNickname": "rabbit",
    "content": "this is content3.",
    "createdDate": "2021-08-31T15:37:54",
    "modifiedDate": "2021-08-31T15:37:54"
}
]*/
data class PostModel(
    @SerializedName(value="id")
    var bookId: Long? = null,
    var type: String = "",
    var scope: String = "",
    val isIncomplete: Boolean = false,
    var imgLocation: String = "",
    var title: String = "",
    var content: String = "",
    val createdDate: String = "",
    val modifiedDate: String = "",
    val likedList: ArrayList<LikedModel> = ArrayList<LikedModel>(),
    val commentsDto: ArrayList<CommentModel> = ArrayList<CommentModel>()
)