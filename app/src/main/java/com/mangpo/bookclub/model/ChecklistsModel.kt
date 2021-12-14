package com.mangpo.bookclub.model

data class ChecklistsModel(
    var data: List<CheckListModel>
)

data class CheckListModel(
    var toDoId: Long,
    var content: String
)
