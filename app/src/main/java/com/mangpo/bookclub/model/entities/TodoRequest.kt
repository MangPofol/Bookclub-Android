package com.mangpo.bookclub.model.entities

data class TodoRequest(
    val contents: List<String>
)

data class UpdateTodoRequest(
    val content: String,
    val isComplete: Boolean
)
