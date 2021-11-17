package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.model.ClubResData
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.ClubService
import retrofit2.Response

class ClubRepository() {
    private val clubService: ClubService = ApiClient.clubService

    /*suspend fun createClub(newClub: ClubModel): Response<ClubModel> {
        return clubService.createClub(newClub)
    }

    suspend fun getClubsByUser(): Response<ClubResData> {
        return clubService.getClubsByUser()
    }*/
}