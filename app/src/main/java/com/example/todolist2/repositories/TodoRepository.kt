package com.example.todolist2.repositories

import com.example.todolist2.database.BuildDatabase
import com.example.todolist2.home.editType
import com.example.todolist2.models.SubTodos
import com.example.todolist2.models.TodoApps
import com.example.todolist2.models.Todos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class TodoRepository(private val db: BuildDatabase) {

    val dataTodos: Flow<List<Todos>> = db.TodoDao().getAll()


    suspend fun addTodoAll(form: Todos): List<Long> = db.TodoDao().insertAll(form)

    suspend fun addSubTodoAll(form: SubTodos): List<Long> = db.SubTodosDao().insertAll(form)
    fun getSubTodosData(id: Int): List<SubTodos> {
        return db.TodoDao().withSubTodos(id)
    }


    suspend fun editSubTodo(id: Int, value: Boolean) {
        db.SubTodosDao().edit(id, value)
    }

    suspend fun deleteSubTodo(subTodo: SubTodos) {
        db.SubTodosDao().delete(subTodo)
    }

    suspend fun editLabelTodo(value: editType) {
        db.TodoDao().editLabel(value.id, value.value)
    }

    suspend fun deleteTodo(it: Todos) {
        db.TodoDao().delete(it)
    }
}