package com.mangpo.bookclub.model

/*{
    "bookId": 290,
    "scope": "CLUB",
    "isIncomplete": false,
    "location": "내 방",
    "readTime": "해질녘",
    "hyperlink": "www.mangpo.com",
    "title": "죽과죽과",
    "content": "꿀잼10",
    "postImgLocations": [
    "image3",
    "image4"
    ],
    "clubIdListForScope": [6]
}*/
data class PostModel(
    var bookId: Long? = null,
    var scope: String? = null,
    var isIncomplete: Boolean = false,
    var location: String? = null,
    var readTime: String? = null,
    var hyperlink: String? = null,
    var title: String? = null,
    var content: String? = null,
    var postImgLocations: List<String> = ArrayList<String>(),
//    var clubIdListForScope: List<Long> = ArrayList<Long>()
)