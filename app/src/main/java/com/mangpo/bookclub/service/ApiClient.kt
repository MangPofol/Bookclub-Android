package com.mangpo.bookclub.service

object ApiClient {
    val userService: UserService by lazy {
        BaseService.getClient().create(UserService::class.java)
    }
    val bookService: BookService by lazy {
        BaseService.getClient().create(BookService::class.java)
    }
    val kakaoBookService: KakaoBookService by lazy {
        BaseService.getKakaoClient().create(KakaoBookService::class.java)
    }
    val postService: PostService by lazy {
        BaseService.getClient().create(PostService::class.java)
    }
    val checklistService: ChecklistService by lazy {
        BaseService.getClient().create(ChecklistService::class.java)
    }

    //베타 버전 출시 후 사용
    val clubService: ClubService by lazy {
        BaseService.getClient().create(ClubService::class.java)
    }

}