package com.mangpo.bookclub.model

import com.google.gson.annotations.SerializedName

data class ChecklistsModel(
    var data: List<CheckListModel>
)

data class ChecklistGroupByMonthModel(
    var groupingDate: String,
    var checklists: ArrayList<CheckListModel>
)

data class CheckListModel(
    @SerializedName("toDoId")
    var toDoId: Long? = null,
    @SerializedName("content")
    var content: String? = null,
    @SerializedName("isComplete")
    var isComplete: Boolean = false,
    @SerializedName("createDate")
    var createDate: String? = null,
    @SerializedName("modifiedDate")
    var modifiedDate: String? = null
)
