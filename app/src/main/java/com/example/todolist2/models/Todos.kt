package com.example.todolist2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Todos",
)
data class Todos(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean = false,
    @ColumnInfo(name = "highlight") val highlight: Long,
    @ColumnInfo(name = "note") val note: String,
)

data class TodoApps(
    val id: Int = 0,
    val label: String,
    val isCompleted: Boolean = false,
    val highlight: Long,
    val note: String,
    val subTodos: List<SubTodos>? = mutableListOf(),
)


@Entity(
    tableName = "SubTodos",
    foreignKeys = [ForeignKey(
        entity = Todos::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("todo_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )]
)
data class SubTodos(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "todo_id") val todo_id: Int? = null,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean = false,
)