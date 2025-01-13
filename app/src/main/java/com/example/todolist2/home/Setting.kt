package com.example.todolist2.home

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist2.Layout
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun Setting() {
    val context = LocalContext.current
    val vm: SettingViewModel = koinViewModel<SettingViewModel>(
        parameters = { parametersOf(context) }
    )

    val state = vm.dataStore.getTheme().collectAsStateWithLifecycle(
        initialValue = "System",
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(state.value) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFF607D8B).copy(0.3f))
                .padding(8.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Dark Mode"
            )
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    Text(text = state.value)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    vm.listDarkMode.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption = option.title
                                vm.changeTheme(option.title)
                                expanded = false
                            },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        painter = painterResource(option.icon),
                                        contentDescription = option.title,
                                        modifier = Modifier
                                            .padding(end = 5.dp),
                                        tint = if (option.title == "Dark") Color(0xFF37474F) else if (option.title == "System") Color.Yellow else Color(
                                            0xFFFBC02D
                                        )
                                    )
                                    Text(text = option.title)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = false,
    device = Devices.PIXEL_2
)
@Composable
fun PreviewSetting() {
    Layout {
        Setting()
    }
}