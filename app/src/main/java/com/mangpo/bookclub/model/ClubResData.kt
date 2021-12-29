package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

//베타 버전 출시 후 사용
data class ClubResData(
    @SerializedName("data")
    var clubs: MutableList<ClubModel>
)
