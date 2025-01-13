package com.example.todolist2.create

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist2.database.BuildDatabase
import com.example.todolist2.models.SubTodos
import com.example.todolist2.models.Todos
import com.example.todolist2.repositories.TodoRepository
import com.example.todolist2.type.TypeConvertor
import com.example.todolist2.ui.theme.LocalColors
import kotlinx.coroutines.launch

data class listColor(
    val color: Color,
    val hex: Long,
)


data class Form(
    val label: String,
    val note: String,
    val highlights: Long,
    val SubTodos: MutableList<SubTodos>,
)

class CreateTodoViewModel(context: Context) : ViewModel() {

    private val repository: TodoRepository
    val convertor: TypeConvertor = TypeConvertor()

    val label: MutableState<String> = mutableStateOf("")
    val note: MutableState<String> = mutableStateOf("")


    init {
        val database = BuildDatabase.getDatabase(context)
        repository = TodoRepository(database)
    }


    val subTasks: MutableList<SubTodos> = mutableStateListOf()

    val listColors: MutableList<listColor> = mutableListOf(
        listColor(
            color = Color(0xFFF5C86F),
            hex = 0xFFF5C86F
        ),
        listColor(
            color = Color(0xFFEBA536),
            hex = 0xFFEBA536
        ),
        listColor(
            color = Color(0xFFEA6A39),
            hex = 0xFFEA6A39
        ),
        listColor(
            color = Color(0xFFED4A3F),
            hex = 0xFFED4A3F
        ),
        listColor(
            color = Color(0xFFD52865),
            hex = 0xFFD52865
        ),
        listColor(
            color = Color(0xFF9130A4),
            hex = 0xFF9130A4
        ),
        listColor(
            color = Color(0xFF8264B2),
            hex = 0xFF8264B2
        ),
        listColor(
            color = Color(0xFF4655AC),
            hex = 0xFF4655AC
        ),
        listColor(
            color = Color(0xFF3494DC),
            hex = 0xFF3494DC
        ),
        listColor(
            color = Color(0xFF39AEDE),
            hex = 0xFF39AEDE
        ),
        listColor(
            color = Color(0xFF14B4C8),
            hex = 0xFF14B4C8
        ),
        listColor(
            color = Color(0xFF0E927E),
            hex = 0xFF0E927E
        ),
        listColor(
            color = Color(0xFF5FA764),
            hex = 0xFF5FA764
        ),
        listColor(
            color = Color(0xFF95C05F),
            hex = 0xFF95C05F
        ),
        listColor(
            color = Color(0xFFCDDB4A),
            hex = 0xFFCDDB4A
        ),
    )

    val highlight: MutableState<Long> = mutableStateOf(0xFF)

    fun addTask(subTodos: SubTodos) {
        subTasks.add(subTodos)
    }


    fun editSubTask(it: Boolean, index: Int) {
        subTasks[index] = subTasks[index].copy(
            isCompleted = it
        )
    }

    fun removeSubTask(item: SubTodos) {
        subTasks.remove(item)
    }


    fun submitForm(form: Form) {
        viewModelScope.launch {
            val response = repository.addTodoAll(
                Todos(
                    label = form.label,
                    highlight = form.highlights,
                    note = form.note
                )
            )

            response.let {
                it.forEach({ item ->
                    form.SubTodos.forEach({ value ->
                        repository.addSubTodoAll(
                            SubTodos(
                                todo_id = item.toInt(),
                                label = value.label,
                                isCompleted = value.isCompleted
                            )
                        )
                    })
                })
            }

        }
    }
}