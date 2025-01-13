package com.example.todolist2.type

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.example.todolist2.models.SubTodos
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConvertor {

    @TypeConverter
    fun fromColorToLong(value: Color): Long = value.value.toLong()

    @TypeConverter
    fun fromLongToColor(value: Long): Color = Color(value)


    private val gson = Gson()


    @TypeConverter
    fun fromSubTodosList(list: MutableList<SubTodos>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toSubTodosList(json: String): MutableList<SubTodos> {
        val type = object : TypeToken<MutableList<SubTodos>>() {}.type
        return gson.fromJson(json, type) // تبدیل JSON به لیست
    }

}