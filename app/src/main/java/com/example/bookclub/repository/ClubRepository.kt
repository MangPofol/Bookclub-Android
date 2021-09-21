package com.example.bookclub.repository

import android.app.Application
import com.example.bookclub.model.ClubModel
import com.example.bookclub.model.ClubResData
import com.example.bookclub.service.ClubService
import retrofit2.Response

class ClubRepository() {
    private val clubService: ClubService = ApiClient.clubService

    suspend fun createClub(newClub: ClubModel): Response<ClubModel> {
        return clubService.createClub(newClub)
    }

    suspend fun getClubsByUser(): Response<ClubResData> {
        return clubService.getClubsByUser()
    }
}