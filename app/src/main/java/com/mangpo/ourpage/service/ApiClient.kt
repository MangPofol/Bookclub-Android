package com.mangpo.ourpage.service

import com.mangpo.ourpage.ApplicationClass

object ApiClient {
    val userService: UserService = ApplicationClass.baseAPI.create(UserService::class.java)
    val todoService: TodoService = ApplicationClass.baseAPI.create(TodoService::class.java)
    val bookService: BookService = ApplicationClass.baseAPI.create(BookService::class.java)
    val kakaoBookService: BookService = ApplicationClass.kakaoAPI.create(BookService::class.java)
    val postService: PostService = ApplicationClass.baseAPI.create(PostService::class.java)
}