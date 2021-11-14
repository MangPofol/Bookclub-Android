package com.mangpo.bookclub.viewmodel

import com.mangpo.bookclub.repository.BookRepository
import com.mangpo.bookclub.repository.KakaoBookRepository
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.BookService
import com.mangpo.bookclub.service.KakaoBookService
import com.mangpo.bookclub.service.UserService
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
    single<UserRepository> {
        UserRepository(get())
    }
    single<BookRepository> {
        BookRepository(get(), get(), get())
    }
    single<KakaoBookRepository> {
        KakaoBookRepository(get())
    }
    viewModel<MainViewModel> {
        MainViewModel(get())
    }
    viewModel<BookViewModel> {
        BookViewModel(get(), get(), get())
    }
    viewModel<PostViewModel> {
        PostViewModel()
    }
}