package com.mangpo.ourpage.repository

import com.mangpo.ourpage.model.entities.TodoRequest
import com.mangpo.ourpage.model.entities.UpdateTodoRequest
import com.mangpo.ourpage.model.remote.TodoResponse
import retrofit2.Response

interface TodoRepository {
    fun getTodos(onResponse: (Response<TodoResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun createTodo(newTodo: TodoRequest, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun updateTodo(toDoId: Int, updateTodo: UpdateTodoRequest, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deleteTodo(toDoId: Int, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
}