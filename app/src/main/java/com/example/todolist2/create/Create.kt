package com.example.todolist2.create

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolist2.HomeScreen
import com.example.todolist2.Layout
import com.example.todolist2.models.SubTodos
import com.example.todolist2.type.TypeConvertor
import com.example.todolist2.ui.theme.LocalColors
import com.example.todolist2.ui.theme.widget.InputField
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Create(navController: NavHostController) {

    val context = LocalContext.current
    val vm: CreateTodoViewModel =
        koinViewModel<CreateTodoViewModel>(parameters = { parametersOf(context) })

    var label by remember {
        vm.label
    }
    var note by remember {
        vm.note
    }
    val showErrors = remember {
        mutableStateListOf<CustomError>()
    }
    Layout(topAppBar = {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Create New Todo"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = LocalColors.current.topAppBarColor,
                titleContentColor = LocalColors.current.textTopBarColor
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            InputField(
                value = label,
                onValueChange = {
                    label = it
                },
                label = {
                    Text(
                        text = "Label"
                    )
                },
                isError = showErrors.filter { it.type == "label" }.isNotEmpty()
            )
            showErrors.find { it.type == "label" }?.let {
                Text(
                    text = it.message,
                    color = LocalColors.current.errorColor,
                    modifier = Modifier.padding(5.dp)
                )
            }
            InputField(
                value = note,
                onValueChange = {
                    note = it
                },
                modifier = Modifier.padding(top = 20.dp),
                label = {
                    Text(
                        text = "Note"
                    )
                },
                isError = showErrors.filter { it.type == "note" }.isNotEmpty()
            )
            showErrors.find { it.type == "note" }?.let {
                Text(
                    text = it.message,
                    color = LocalColors.current.errorColor,
                    modifier = Modifier.padding(5.dp)
                )
            }
            subTodo(vm)
            colorPicker(vm)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        if (label.isEmpty()) {
                            showErrors.add(
                                CustomError(
                                    message = "Field label is Required",
                                    type = "label"
                                )
                            )
                        } else {
                            showErrors.clear()
                        }
                        if (note.isEmpty()) {
                            showErrors.add(
                                CustomError(
                                    message = "Field note is Required",
                                    type = "note"
                                )
                            )
                        } else {
                            showErrors.clear()
                        }

                        if (showErrors.none { it.type == "label" } && showErrors.none { it.type == "note" }) {
                            vm.submitForm(
                                Form(
                                    label = label,
                                    note = note,
                                    highlights = vm.highlight.value,
                                    SubTodos = vm.subTasks
                                )
                            )
                            navController.navigate(HomeScreen)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonColors(
                        containerColor = LocalColors.current.floatingColorContent,
                        contentColor = Color.White,
                        disabledContainerColor = LocalColors.current.floatingColorContent.copy(0.5f),
                        disabledContentColor = Color.White.copy(0.5F)
                    )
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add",
                            tint = Color.White
                        )
                        Text(
                            text = "Create",
                            color = Color.White
                        )
                    }
                }
            }
        }


    }
}


data class CustomError(
    val message: String,
    val type: String,
)

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun subTodo(vm: CreateTodoViewModel) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var label by remember {
        mutableStateOf("")
    }

    var isCompleted by remember {
        mutableStateOf(false)
    }

    val subTask = remember {
        vm.subTasks
    }

    val showErrors = remember {
        mutableStateListOf<CustomError>()
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            IconButton(
                onClick = {
                    showDialog = !showDialog
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = "Sub Tasks", fontSize = 20.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
            if (subTask.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column {
                        subTask.forEachIndexed({ index, item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(checked = item.isCompleted, onCheckedChange = {
                                        vm.editSubTask(it, index)
                                    })
                                    Text(
                                        text = item.label
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        vm.removeSubTask(item)
                                    }, colors = IconButtonDefaults.iconButtonColors().copy(
                                        contentColor = Color.Red
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        })
                    }

                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(icon = {}, title = {
            Text(
                text = "Add New Sub Task"
            )
        }, text = {
            Column {
                InputField(
                    value = label,
                    onValueChange = {
                        label = it
                    },
                    label = {
                        Text(
                            text = "Sub Task"
                        )
                    },
                    isError = showErrors.filter { it.type == "label" }.isNotEmpty()
                )
                showErrors.find { it.type == "label" }?.let {
                    Text(
                        text = it.message,
                        color = LocalColors.current.errorColor,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isCompleted, onCheckedChange = {
                        isCompleted = it
                    })
                    Text(
                        text = "is Completed",
                    )
                }
            }
        }, onDismissRequest = {
            showDialog = false
        }, confirmButton = {
            TextButton(onClick = {
                if (label.isEmpty()) {
                    showErrors.add(
                        CustomError(
                            message = "Field label is Required.",
                            type = "label"
                        )
                    )
                } else {
                    showErrors.clear()
                }

                if (showErrors.filter { it.type == "label" }.isEmpty()) {
                    vm.addTask(
                        SubTodos(
                            label = label, isCompleted = isCompleted
                        )
                    )
                    label = ""
                    isCompleted = false
                    showDialog = false
                }
            }) {
                Text("Confirm")
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDialog = false
            }) {
                Text("Dismiss")
            }
        })
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun colorPicker(vm: CreateTodoViewModel) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val color = LocalColors.current.highlightDefault
    var highlight by remember {
        mutableStateOf<Color>(color)
    }


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            IconButton(
                onClick = {
                    showDialog = !showDialog
                }, modifier = Modifier
                    .fillMaxWidth()
                    .size(60.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(highlight.copy(0.5F))
                            .size(50.dp)
                            .border(
                                BorderStroke(
                                    width = 5.dp,
                                    color = highlight
                                ),
                                shape = CircleShape
                            ),
                    ) {}
                    Text(
                        text = "Highlight",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 5.dp)
                    )
                }
            }

        }
    }

    if (showDialog) {
        AlertDialog(icon = {}, title = {
            Text(
                text = "Color", color = highlight
            )
        }, text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
            ) {
                items(items = vm.listColors) { item ->
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(item.color.copy(0.5f))
                            .size(60.dp)
                            .border(
                                BorderStroke(3.dp, item.color), shape = CircleShape
                            )
                            .clickable {
                                highlight = item.color
                                vm.highlight.value = item.hex
                            },
                    ) {}
                }
            }
        }, onDismissRequest = {
            showDialog = false
        }, confirmButton = {
            TextButton(onClick = {
                showDialog = false
            }) {
                Text("Confirm")
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDialog = false
            }) {
                Text("Dismiss")
            }
        })
    }
}