package com.mangpo.bookclub.service

object ApiClient {

    private const val BASE_URL = com.mangpo.bookclub.BuildConfig.BASE_URL
    private const val KAKAO_BOOK_API_BASE_URL =
        com.mangpo.bookclub.BuildConfig.KAKAO_BOOK_API_BASE_URL

    val userService: UserService by lazy {
        BaseService.getClient(BASE_URL).create(UserService::class.java)
    }
    val bookService: BookService by lazy {
        BaseService.getClient(BASE_URL).create(BookService::class.java)
    }
    val kakaoBookService: KakaoBookService by lazy {
        BaseService.getKakaoClient(KAKAO_BOOK_API_BASE_URL).create(KakaoBookService::class.java)
    }
    val postService: PostService by lazy {
        BaseService.getClient(BASE_URL).create(PostService::class.java)
    }
    val checklistService: ChecklistService by lazy {
        BaseService.getClient(BASE_URL).create(ChecklistService::class.java)
    }

    val clubService: ClubService by lazy {
        BaseService.getClient(BASE_URL).create(ClubService::class.java)
    }

}