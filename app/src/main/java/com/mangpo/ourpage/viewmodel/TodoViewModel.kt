package com.mangpo.ourpage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.ourpage.model.entities.TodoRequest
import com.mangpo.ourpage.model.entities.UpdateTodoRequest
import com.mangpo.ourpage.model.remote.Todo
import com.mangpo.ourpage.model.remote.TodoGroupByMonth
import com.mangpo.ourpage.repository.TodoRepositoryImpl
import java.text.SimpleDateFormat

class TodoViewModel: BaseViewModel() {
    private val todoRepositoryImpl: TodoRepositoryImpl = TodoRepositoryImpl()

    private val _unCompleteTodos: MutableLiveData<List<Todo>> = MutableLiveData()
    val unCompleteTodos: LiveData<List<Todo>> get() = _unCompleteTodos

    private val _completeTodos: MutableLiveData<List<TodoGroupByMonth>> = MutableLiveData()
    val completeTodos: LiveData<List<TodoGroupByMonth>> get() = _completeTodos

    private val _createTodoCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val createTodoCode: LiveData<Event<Int>> get() = _createTodoCode

    private val _updateTodoCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val updateTodoCode: LiveData<Event<Int>> get() = _updateTodoCode

    private val _completeTodoCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val completeTodoCode: LiveData<Event<Int>> get() = _completeTodoCode

    private val _deleteTodoCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val deleteTodoCode: LiveData<Event<Int>> get() = _deleteTodoCode

    //월별로 완료된 체크리스트 그룹화하기
    private fun groupByDate(checklist: List<Todo>?): ArrayList<TodoGroupByMonth> {
        val checklistGroupByMonthModel: ArrayList<TodoGroupByMonth> = arrayListOf()

        checklist?.forEach { it ->
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val output = SimpleDateFormat("yyyy.MM")
            val dateStr = output.format(input.parse(it.createDate))

            val element = checklistGroupByMonthModel.find { it.groupingDate == dateStr }
            if (element == null) {
                checklistGroupByMonthModel.add(TodoGroupByMonth(dateStr, arrayListOf(it)))
            } else {
                val idx = checklistGroupByMonthModel.indexOf(element)
                checklistGroupByMonthModel[idx].checklists.add(it)
            }
        }

        return checklistGroupByMonthModel
    }

    fun getTodos() {
        todoRepositoryImpl.getTodos(
            onResponse = {
                Log.d("TodoViewModel", "getTodos Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==200) {
                    _completeTodos.value = groupByDate(it.body()!!.data.filter { it.isComplete })
                    _unCompleteTodos.value = it.body()!!.data.filter { !it.isComplete }
                }
            },
            onFailure = {
                Log.e("TodoViewModel", "getTodos Fail!\nmessage: ${it.message}")
            }
        )
    }

    fun createTodo(newTodo: String) {
        val req: TodoRequest = TodoRequest(listOf(newTodo))

        todoRepositoryImpl.createTodo(
            newTodo = req,
            onResponse = {
                Log.d("TodoViewModel", "createTodo Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _createTodoCode.value = Event(it.code())
            },
            onFailure = {
                Log.e("TodoViewModel", "createTodo Fail!\nmessage: ${it.message}")
                _createTodoCode.value = Event(600)
            }
        )
    }

    fun updateTodo(toDoId: Int, content: String, isComplete: Boolean) {
        val updateTodo: UpdateTodoRequest = UpdateTodoRequest(content=content, isComplete = isComplete)

        todoRepositoryImpl.updateTodo(
            toDoId = toDoId,
            updateTodo = updateTodo,
            onResponse = {
                Log.d("TodoViewModel", "updateTodo Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                if (isComplete)
                    _completeTodoCode.value = Event(it.code())
                else
                _updateTodoCode.value = Event(it.code())
            },
            onFailure = {
                Log.e("TodoViewModel", "updateTodo Fail!\nmessage: ${it.message}")
                if (isComplete)
                    _completeTodoCode.value = Event(600)
                else
                    _updateTodoCode.value = Event(600)
            }
        )
    }

    fun deleteTodo(toDoId: Int) {
        todoRepositoryImpl.deleteTodo(
            toDoId = toDoId,
            onResponse = {
                Log.d("TodoViewModel", "deleteTodo Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _deleteTodoCode.value = Event(it.code())
            },
            onFailure = {
                _deleteTodoCode.value = Event(600)
            }
        )
    }
}