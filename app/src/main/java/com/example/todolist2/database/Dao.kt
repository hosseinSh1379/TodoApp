package com.example.todolist2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todolist2.models.SubTodos
import com.example.todolist2.models.Todos
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todos")
    fun getAll(): Flow<List<Todos>>

    @Query("SELECT * FROM Todos WHERE id IN (:Todos)")
    fun loadAllByIds(Todos: IntArray): List<Todos>

    @Query("SELECT * FROM SubTodos WHERE todo_id IN (:todo_id)")
    fun withSubTodos(todo_id: Int): List<SubTodos>


    @Query(
        "SELECT * FROM Todos WHERE label LIKE :label LIMIT 1"
    )
    fun findByName(label: String): Todos

    @Insert
    fun insertAll(vararg todos: Todos): List<Long>

    @Delete
    fun delete(Todo: Todos)

    @Query("UPDATE Todos SET label = :value WHERE id = :id")
    fun editLabel(id: Int, value: String)
}

@Dao
interface SubTodosDao {
    @Query("SELECT * FROM SubTodos")
    fun getAll(): List<SubTodos>

    @Query("SELECT * FROM SubTodos WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<SubTodos>

    @Query(
        "SELECT * FROM SubTodos WHERE label LIKE :label  LIMIT 1"
    )
    fun findByName(label: String): SubTodos

    @Insert
    fun insertAll(vararg todos: SubTodos): List<Long>

    @Delete
    fun delete(SupTodo: SubTodos)

    @Query("UPDATE SubTodos SET isCompleted = :value WHERE id = :id")
    fun edit(id: Int, value: Boolean)
}