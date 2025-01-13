package com.example.todolist2

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist2.create.Create
import com.example.todolist2.home.Home
import kotlinx.serialization.Serializable


@Composable
fun Nav() {
    val navController = rememberNavController()

    NavHost(startDestination = HomeScreen, navController = navController) {
        appRoutes(navController)
    }
}

fun NavGraphBuilder.appRoutes(navController: NavHostController) {
    composable<HomeScreen> {
        Home(navController)
    }
    composable<CreateScreen> {
        Create(navController)
    }
}


@Serializable
object HomeScreen

@Serializable
object CreateScreen

