package com.mangpo.bookclub.viewmodel

import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.UserService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    single<UserService> {
        ApiClient.userService
    }
    single<UserRepository> {
        UserRepository(get())
    }
    viewModel {
        MainViewModel(get())
    }
}