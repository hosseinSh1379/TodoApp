package com.example.todolist2.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolist2.BottomBarViewModel
import com.example.todolist2.CreateScreen
import com.example.todolist2.Layout
import com.example.todolist2.R
import com.example.todolist2.create.CustomError
import com.example.todolist2.models.SubTodos
import com.example.todolist2.models.TodoApps
import com.example.todolist2.type.TypeConvertor
import com.example.todolist2.ui.theme.LocalColors
import com.example.todolist2.ui.theme.widget.InputField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController? = null) {
    val bottomBarViewModel: BottomBarViewModel = koinViewModel<BottomBarViewModel>()
    val context = LocalContext.current
    val vm: HomeViewModel = koinViewModel<HomeViewModel>(parameters = { parametersOf(context) })
    val current by remember {
        bottomBarViewModel.current
    }

    val snackbarHostState = remember { SnackbarHostState() }


    Layout(
        topAppBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = current
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LocalColors.current.topAppBarColor,
                    titleContentColor = LocalColors.current.textTopBarColor
                ), modifier = Modifier.fillMaxWidth()
            )
        },
        bottomAppBar = {
            bottomBarViewModel.render()
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = {
                        navController?.navigate(route = CreateScreen)
                    },
                    containerColor = LocalColors.current.floatingColorContent,
                    modifier = Modifier.offset(
                        x = 10.dp, y = 43.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                        actionColor = LocalColors.current.textTopBarColor,
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = LocalColors.current.listBottomBarColor
                    )
                }
            )
        }

    ) {
        when (current) {
            "Home" -> ListTodos(vm, snackbarHostState)
            "Settings" -> Setting()
            else -> Text("not Set")
        }
    }
}


@SuppressLint("ShowToast", "CoroutineCreationDuringComposition")
@Composable
fun ListTodos(vm: HomeViewModel, snackbarHostState: SnackbarHostState) {

    var data = vm.data.collectAsState()

    var dragItem by remember {
        vm.onDargItem
    }
    var view by remember {
        vm.onSelectView
    }

    var isActionClicked by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    dragItem?.let {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = "${it.label}, successfully deleted.",
                actionLabel = "Undo"
            )
            if (result == SnackbarResult.ActionPerformed) {
                vm.undo()
                isActionClicked = true
                dragItem = null
            }
        }
        scope.launch {
            delay(3000)
            if (!isActionClicked) {
                snackbarHostState.currentSnackbarData?.dismiss()
                vm.deleteTodo(it)
                dragItem = null
            }
            isActionClicked = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
    ) {
        if (data.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Empty List",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            LazyColumn {
                itemsIndexed(items = data.value, key = { _, item ->
                    item.hashCode()
                }) { index, item ->
                    CardLayout(item, vm)
                }
            }
        }
    }

    view?.let {
        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = LocalColors.current.textTopBarColor
                )
            },
            title = {
                Text(
                    text = it.label
                )
            },
            text = {
                Text(
                    text = it.note
                )
            },
            onDismissRequest = {
                view = null
            },
            confirmButton = {

            },
            dismissButton = {
                TextButton(onClick = {
                    view = null
                }) {
                    Text("Close")
                }
            })
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CardLayout(it: TodoApps, vm: HomeViewModel) {

    var edit = remember {
        vm.edit
    }
    val scope = rememberCoroutineScope()


    val offsetX = remember { Animatable(0f) }
    val threshold = 300f

    Box(
        modifier = Modifier
            .padding(top = 15.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .background(
                if (it.highlight.toInt() == 255) LocalColors.current.highlightDefault.copy(
                    0.5f
                ) else
                    TypeConvertor().fromLongToColor(it.highlight).copy(0.5f)
            )
            .fillMaxSize()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = LocalColors.current.dangerColor
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(onDrag = { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        val newOffset = offsetX.value + dragAmount.x
                        if (newOffset >= 0f) {
                            offsetX.snapTo(newOffset)
                        }
                    }
                }, onDragEnd = {
                    scope.launch {
                        if (offsetX.value > threshold) {
                            offsetX.animateTo(1000f, animationSpec = tween(300))
                            vm.onDargItem.value = it
                            vm.minuteDeletion(it)
                        } else {
                            offsetX.animateTo(0f, animationSpec = tween(300))
                        }
                    }
                })
            }
            .offset { IntOffset(offsetX.value.toInt(), 0) }
            .background(Color(0xFF607D8B))) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = {
                        vm.onSelectView.value = it
                    },
                    modifier = Modifier
                        .offset(
                            x = (-10).dp
                        )

                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_remove_red_eye_24),
                        contentDescription = "Show"
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (it.highlight.toInt() == 255) LocalColors.current.highlightDefault.copy(
                            0.5f
                        ) else
                            TypeConvertor().fromLongToColor(it.highlight).copy(0.5f)
                    )
                    .padding(
                        start = 30.dp
                    )
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (vm.findIndex(it.id) != -1) {
                    Row {
                        IconButton(
                            onClick = {
                                vm.editLabelTodo(
                                    editType(
                                        id = it.id, value = vm.edit[vm.findIndex(it.id)].value
                                    )
                                )
                            }, colors = IconButtonDefaults.iconButtonColors().copy(
                                contentColor = LocalColors.current.editBottomColor
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit",
                            )
                        }
                        InputField(
                            value = vm.edit[vm.findIndex(it.id)].value, onValueChange = { newText ->
                                vm.edit[vm.findIndex(it.id)] = vm.edit[vm.findIndex(it.id)].copy(
                                    value = newText
                                )
                            }, modifier = Modifier.wrapContentSize()
                        )

                    }
                } else {
                    Text(text = it.label, modifier = Modifier.clickable {
                        vm.edit.add(
                            editType(
                                id = it.id, value = it.label
                            )
                        )
                    })
                    AddSubTodos(vm, it)
                }
            }
        }
    }
    if (it.subTodos != null) {
        if (it.subTodos.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(
                        x = 0.dp, y = (-5).dp
                    )
                    .padding(start = 25.dp, end = 25.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 20.dp, bottomStart = 20.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.secondary.copy(0.8F))
                    .defaultMinSize(minHeight = 60.dp)
                    .wrapContentHeight()
                    .padding(15.dp)
            ) {
                Column {
                    it.subTodos.forEachIndexed({ index, subTodo ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Ltr(subTodo = subTodo, onChangeValue = {
                                    vm.updateSubTodo(subTodo, it, index)
                                })
                            }
                            IconButton(
                                onClick = {
                                    vm.deleteSubTodo(subTodo)
                                }, colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = LocalColors.current.dangerColor
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


@Composable
fun AddSubTodos(vm: HomeViewModel, todoApps: TodoApps) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var label by remember {
        mutableStateOf("")
    }

    var isCompleted by remember {
        mutableStateOf(false)
    }
    val showErrors = remember {
        mutableStateListOf<CustomError>()
    }

    IconButton(
        onClick = {
            showDialog = !showDialog
        }, colors = IconButtonDefaults.iconButtonColors(
            contentColor = LocalColors.current.textTopBarColor
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = "Add"
        )
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
                        text = "is Completed"
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
                            message = "Field label is required",
                            type = "label"
                        )
                    )
                } else {
                    showErrors.clear()
                }

                if (showErrors.none { it.type == "label" }) {
                    vm.addTaskDb(
                        SubTodos(
                            label = label, isCompleted = isCompleted, todo_id = todoApps.id
                        ), todoApps
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

@Composable
fun Rtl(
    subTodo: SubTodos,
    onChangeValue: (Boolean) -> Unit,
) {
    Text(
        text = subTodo.label
    )
    Checkbox(
        checked = subTodo.isCompleted, onCheckedChange = onChangeValue, colors = CheckboxColors(
            checkedCheckmarkColor = LocalColors.current.checkedCheckmarkColor,
            uncheckedCheckmarkColor = LocalColors.current.uncheckedCheckmarkColor,
            checkedBoxColor = LocalColors.current.checkedBoxColor,
            uncheckedBoxColor = LocalColors.current.uncheckedBoxColor,
            disabledCheckedBoxColor = LocalColors.current.disabledCheckedBoxColor,
            disabledUncheckedBoxColor = LocalColors.current.disabledUncheckedBoxColor,
            disabledIndeterminateBoxColor = LocalColors.current.disabledIndeterminateBoxColor,
            checkedBorderColor = LocalColors.current.checkedBorderColor,
            uncheckedBorderColor = LocalColors.current.uncheckedBorderColor,
            disabledBorderColor = LocalColors.current.disabledBorderColor,
            disabledUncheckedBorderColor = LocalColors.current.disabledUncheckedBorderColor,
            disabledIndeterminateBorderColor = LocalColors.current.disabledIndeterminateBorderColor
        )
    )
}

@Composable
fun Ltr(
    subTodo: SubTodos,
    onChangeValue: (Boolean) -> Unit,
) {
    Checkbox(
        checked = subTodo.isCompleted,
        onCheckedChange = onChangeValue,
        colors = CheckboxColors(
            checkedCheckmarkColor = LocalColors.current.checkedCheckmarkColor,
            uncheckedCheckmarkColor = LocalColors.current.uncheckedCheckmarkColor,
            checkedBoxColor = LocalColors.current.checkedBoxColor,
            uncheckedBoxColor = LocalColors.current.uncheckedBoxColor,
            disabledCheckedBoxColor = LocalColors.current.disabledCheckedBoxColor,
            disabledUncheckedBoxColor = LocalColors.current.disabledUncheckedBoxColor,
            disabledIndeterminateBoxColor = LocalColors.current.disabledIndeterminateBoxColor,
            checkedBorderColor = LocalColors.current.checkedBorderColor,
            uncheckedBorderColor = LocalColors.current.uncheckedBorderColor,
            disabledBorderColor = LocalColors.current.disabledBorderColor,
            disabledUncheckedBorderColor = LocalColors.current.disabledUncheckedBorderColor,
            disabledIndeterminateBorderColor = LocalColors.current.disabledIndeterminateBorderColor
        ),
    )
    Text(
        text = subTodo.label
    )
}


@Preview(
    showBackground = true, showSystemUi = true, device = Devices.PIXEL_2
)
@Composable
fun PreviewHome() {
    Home()
}