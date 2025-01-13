package com.example.todolist2

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.todolist2.create.CreateTodoViewModel
import com.example.todolist2.home.HomeViewModel
import com.example.todolist2.home.SettingViewModel
import com.example.todolist2.ui.theme.TodoList2Theme
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.util.prefs.Preferences

val appModule = module {
    viewModel { (context: Context) -> HomeViewModel(context) }
    viewModel { BottomBarViewModel() }
    viewModel { (context: Context) -> CreateTodoViewModel(context) }
    viewModel { (context: Context) -> SettingViewModel(context) }
}

class MyApplicationSetup : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoList2Theme(
                context = this@MainActivity
            ) {
                Nav()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(
    showBackground = true, showSystemUi = true, device = Devices.PIXEL_2
)
@Composable
fun GreetingPreview() {
    MainActivity()
}

