package com.mangpo.ourpage.model.remote

data class TodoResponse(
    val data: ArrayList<Todo>
)

data class Todo(
    val toDoId: Int,
    val content: String,
    val isComplete: Boolean,
    val createDate: String,
    val modifiedDate: String
)

data class TodoGroupByMonth(
    var groupingDate: String,
    var checklists: ArrayList<Todo>
)
