package com.mangpo.bookclub.model

/*{
    "id":523,
    "scope":"PRIVATE",
    "isIncomplete":false,
    "imgLocation":null,
    "title":"돌이킬 수 없는 약속",
    "content":"글 설정 데이터 추가 테스트",
    "createdDate":"2021-11-17T13:27:34.39063",
    "modifiedDate":"2021-11-17T13:27:34.39063",
    "location":"우리집",
    "readTime":"밤",
    "hyperlink":"https://www.naver.com",
    "postImgLocations":[
        "https://elasticbeanstalk-ap-northeast-2-081654662783.s3.ap-northeast-2.amazonaws.com/static/c2a06f4c-79e6-41f0-8d10-28ab518539f6_1637155593405"
    ],
    "postScopeClub":{},
    "likedList":[],
    "commentsDto":[]
}*/
data class PostDetailModel(
    var id: Long,
    var scope: String,
    var isIncomplete: Boolean,
    var imgLocation: String?,
    var title: String,
    var content: String,
    var createdDate: String,
    var modifiedDate: String,
    var location: String?,
    var readTime: String?,
    var hyperlink: String?,
    var postImgLocations: List<String> = ArrayList<String>(),
    var postScopeClub: ClubModel? = null,
    var likedList: List<String> = ArrayList<String>(),
    var commentsDto: List<String> = ArrayList<String>()
)
