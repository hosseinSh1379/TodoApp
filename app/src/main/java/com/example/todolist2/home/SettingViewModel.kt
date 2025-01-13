package com.example.todolist2.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist2.R
import com.example.todolist2.data.DataStoreLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class ThemeList(
    val title: String,
    val icon: Int,
)

class SettingViewModel(private val context: Context) : ViewModel() {
    val dataStore: DataStoreLocal = DataStoreLocal(context)


    val listDarkMode: MutableList<ThemeList> = mutableListOf(
        ThemeList(
            icon = R.drawable.baseline_dark_mode_24,
            title = "Dark",
        ),
        ThemeList(
            icon = R.drawable.baseline_wb_sunny_24,
            title = "Light",
        ),
        ThemeList(
            icon = R.drawable.baseline_brightness_auto_24,
            title = "System",
        ),
    )

    fun changeTheme(theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveTheme(theme)
        }
    }
}