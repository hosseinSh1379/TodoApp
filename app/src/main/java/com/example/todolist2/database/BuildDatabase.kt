package com.example.todolist2.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist2.models.SubTodos
import com.example.todolist2.models.Todos
import com.example.todolist2.type.TypeConvertor

@Database(entities = [Todos::class, SubTodos::class], version = 1, exportSchema = false)
@TypeConverters(TypeConvertor::class)
abstract class BuildDatabase : RoomDatabase() {
    abstract fun TodoDao(): TodoDao
    abstract fun SubTodosDao(): SubTodosDao

    companion object {
        @Volatile
        private var INSTANCE: BuildDatabase? = null

        fun getDatabase(context: Context): BuildDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BuildDatabase::class.java,
                    "Todos_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            db.execSQL("PRAGMA foreign_keys=ON;")
                        }
                    }).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }

        }
    }
}