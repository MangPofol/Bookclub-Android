package com.mangpo.bookclub.viewmodel

import com.mangpo.bookclub.repository.*
import com.mangpo.bookclub.service.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    single<UserService> {
        ApiClient.userService
    }
    single<BookService> {
        ApiClient.bookService
    }
    single<KakaoBookService> {
        ApiClient.kakaoBookService
    }
    single<PostService> {
        ApiClient.postService
    }
    single<ChecklistService> {
        ApiClient.checklistService
    }
    single<UserRepository> {
        UserRepository(get())
    }
    single<BookRepository> {
        BookRepository(get(), get(), get())
    }
    single<KakaoBookRepository> {
        KakaoBookRepository(get())
    }
    single<PostRepository> {
        PostRepository(get())
    }
    single<ChecklistRepository> {
        ChecklistRepository(get())
    }
    viewModel<MainViewModel> {
        MainViewModel(get())
    }
    viewModel<BookViewModel> {
        BookViewModel(get(), get(), get())
    }
    viewModel<PostViewModel> {
        PostViewModel(get())
    }
    viewModel<ChecklistViewModel> {
        ChecklistViewModel(get())
    }
}