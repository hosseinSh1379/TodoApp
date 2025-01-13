package com.example.todolist2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import com.example.todolist2.ui.theme.LocalColors

data class BottomBarList(
    val icon: ImageVector,
    val title: String,
)

class BottomBarViewModel : ViewModel() {

    var current: MutableState<String> = mutableStateOf("Home")

    val listBottomBar: MutableList<BottomBarList> = mutableListOf(
        BottomBarList(
            icon = Icons.Default.Home, title = "Home"
        ),
        BottomBarList(
            icon = Icons.Default.Settings, title = "Settings"
        )
    )

    @Composable
    fun render() {
        BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = LocalColors.current.bottomBarColor,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 15.dp,
                        end = 15.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listBottomBar.forEach({
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClickLabel = it.title,
                            ) {
                                current.value = it.title
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))

                                .padding(5.dp),
                        ) {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.title,
                                tint = if (current.value == it.title) LocalColors.current.activeBottomColor else LocalColors.current.unActiveBottomColor.copy(0.6F)
                            )
                        }
                        Text(
                            text = it.title,
                            color = if (current.value == it.title) LocalColors.current.activeBottomColor else LocalColors.current.unActiveBottomColor.copy(0.6F)
                        )
                    }
                })

            }
        }
    }
}