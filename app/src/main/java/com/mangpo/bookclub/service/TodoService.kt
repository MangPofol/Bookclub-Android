package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.entities.TodoRequest
import com.mangpo.bookclub.model.entities.UpdateTodoRequest
import com.mangpo.bookclub.model.remote.TodoResponse
import retrofit2.Call
import retrofit2.http.*

interface TodoService {
    @GET("/todos")
    fun getTodos(): Call<TodoResponse>

    @POST("/todos/create-todos")
    fun createTodo(@Body newTodo: TodoRequest): Call<Void>

    @PUT("/todos/{toDoId}")
    fun updateTodo(@Body updateTodo: UpdateTodoRequest, @Path("toDoId") toDoId: Int): Call<Void>

    @DELETE("/todos/{toDoId}")
    fun deleteTodo(@Path("toDoId") toDoId: Int): Call<Void>
}