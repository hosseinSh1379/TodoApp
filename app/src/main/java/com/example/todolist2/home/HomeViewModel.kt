package com.example.todolist2.home

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist2.create.subTodo
import com.example.todolist2.database.BuildDatabase
import com.example.todolist2.models.SubTodos
import com.example.todolist2.models.TodoApps
import com.example.todolist2.models.Todos
import com.example.todolist2.repositories.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


data class editType(
    val id: Int,
    val value: String,
)

class HomeViewModel(context: Context) : ViewModel() {
    private val _data = MutableStateFlow<List<TodoApps>>(emptyList())
    private val todoRepository: TodoRepository
    val data: StateFlow<List<TodoApps>> get() = _data.asStateFlow()
    val edit: MutableList<editType> = mutableStateListOf()
    var onDargItem: MutableState<TodoApps?> = mutableStateOf(null)

    val onSelectView: MutableState<TodoApps?> = mutableStateOf(null)

    init {
        val database = BuildDatabase.getDatabase(context)
        todoRepository = TodoRepository(database)
        loadPage()
    }


    fun loadPage() {
        viewModelScope.launch {
            todoRepository.dataTodos.map { todos ->
                todos.map {
                    TodoApps(
                        id = it.id,
                        label = it.label,
                        isCompleted = it.isCompleted,
                        highlight = it.highlight,
                        note = it.note,
                        subTodos = todoRepository.getSubTodosData(it.id)
                    )
                }
            }.catch { e ->
                println("Error: ${e.message}")
                error("${e.message}")
            }.collect({
                _data.value = it
            })
        }
    }

    fun updateSubTodo(subTodo: SubTodos, isCompleted: Boolean, index: Int) {
        viewModelScope.launch {
            todoRepository.editSubTodo(subTodo.id, isCompleted)

            val currentData = _data.value.toMutableList()
            val todoIndex = currentData.indexOfFirst { it.id == subTodo.todo_id }
            if (todoIndex != -1) {
                val updatedTodo =
                    currentData[todoIndex].copy(subTodos = currentData[todoIndex].subTodos?.map {
                        if (it.id == subTodo.id) it.copy(isCompleted = isCompleted) else it
                    })
                currentData[todoIndex] = updatedTodo
                _data.value = currentData
            }
        }
    }


    fun deleteSubTodo(subTodo: SubTodos) {
        viewModelScope.launch {
            todoRepository.deleteSubTodo(subTodo)

            val currentData = _data.value.toMutableList()
            val todoIndex = currentData.indexOfFirst { it.id == subTodo.todo_id }

            if (todoIndex != -1) {
                val updatedTodo =
                    _data.value[todoIndex].copy(subTodos = _data.value[todoIndex].subTodos?.filter { it.id != subTodo.id })
                val updatedList = _data.value.toMutableList()
                updatedList[todoIndex] = updatedTodo
                _data.value = updatedList
            }
        }
    }

    fun addTaskDb(subTodos: SubTodos, todoApps: TodoApps) {
        viewModelScope.launch {
            todoRepository.addSubTodoAll(subTodos)
            val todoIndex = _data.value.indexOfFirst { it.id == todoApps.id }

            if (todoIndex != -1) {
                val updatedTodo = _data.value[todoIndex].copy(
                    subTodos = _data.value[todoIndex].subTodos.orEmpty() + subTodos
                )
                val updatedList = _data.value.toMutableList()
                updatedList[todoIndex] = updatedTodo
                _data.value = updatedList
            }
        }
    }

    fun findIndex(id: Int): Int {
        return edit.indexOfFirst { it.id == id }
    }

    fun editLabelTodo(value: editType) {
        viewModelScope.launch {
            todoRepository.editLabelTodo(value)
            val todoIndex = _data.value.indexOfFirst { it.id == value.id }
            if (todoIndex != -1) {
                val updatedTodo = _data.value[todoIndex].copy(
                    label = value.value
                )
                val updatedList = _data.value.toMutableList()
                updatedList[todoIndex] = updatedTodo
                _data.value = updatedList
                edit.remove(value)
            }
        }
    }

    fun deleteTodo(it: TodoApps) {
        viewModelScope.launch {
            val currentData = _data.value.toMutableList()
            todoRepository.deleteTodo(
                Todos(
                    id = it.id,
                    label = it.label,
                    isCompleted = it.isCompleted,
                    highlight = it.highlight,
                    note = it.note
                )
            )
            if (it.subTodos != null) {
                it.subTodos.forEach({ subTodo ->
                    deleteSubTodo(subTodo)
                })
            }
            _data.value = _data.value.filter { todo -> todo.id != it.id }
        }
    }

    fun minuteDeletion(it: TodoApps) {
        _data.value = _data.value.filter { todo -> todo.id != it.id }
    }

    fun undo() {
        loadPage()
    }
}