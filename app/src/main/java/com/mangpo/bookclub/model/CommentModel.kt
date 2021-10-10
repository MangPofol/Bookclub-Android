package com.mangpo.bookclub.model

/*"userNickname": "rabbit",
"content": "this is content2.",
"createdDate": "2021-08-31T15:37:21",
"modifiedDate": "2021-08-31T15:37:21"*/
data class CommentModel(
    val userNickname: String = "",
    val content: String = "",
    val createdDate: String = "",
    val modifiedDate: String = ""
)
