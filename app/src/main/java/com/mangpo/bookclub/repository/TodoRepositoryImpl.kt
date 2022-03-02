package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.TodoRequest
import com.mangpo.bookclub.model.entities.UpdateTodoRequest
import com.mangpo.bookclub.model.remote.TodoResponse
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.TodoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoRepositoryImpl: TodoRepository {
    private val todoService: TodoService = ApiClient.todoService

    override fun getTodos(
        onResponse: (Response<TodoResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        todoService.getTodos().enqueue(object : Callback<TodoResponse> {
            override fun onResponse(call: Call<TodoResponse>, response: Response<TodoResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<TodoResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun createTodo(
        newTodo: TodoRequest,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        todoService.createTodo(newTodo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun updateTodo(
        toDoId: Int,
        updateTodo: UpdateTodoRequest,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        todoService.updateTodo(updateTodo, toDoId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun deleteTodo(
        toDoId: Int,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        todoService.deleteTodo(toDoId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}