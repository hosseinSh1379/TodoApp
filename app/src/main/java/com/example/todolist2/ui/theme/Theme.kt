package com.example.todolist2.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.todolist2.data.DataStoreLocal


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = Color(0xFF37474F),
    tertiary = Color.White,
    background = Color(0xFF263238),
    onPrimary = Color(0xFF1976D2),
    onPrimaryContainer = Color(0xFF304FFE),
)


private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = Color(0xFFCFD8DC),
    tertiary = Color.Black,
    background = Color(0xFFE0E0E0),
    onPrimary = Color(0xFF1976D2),
    onPrimaryContainer = Color(0xFFF57C00),
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val dark = CustomColor(
    topAppBarColor = Color(0xFF263238),
    bottomBarColor = Color(0xFF37474F),
    listBottomBarColor = Color(0xFFB0BEC5),
    activeBottomColor = Color(0xFF40C4FF),
    dangerColor = Color(0xFFEF5350),
    errorColor = Color(0xFFB71C1C),
    successColor = Color(0xFF2E7D32),
    iconColor = Color(0xFF4FC3F7),
    textTopBarColor = Color(0xFF03A9F4),
    floatingColorContent = Color(0xFF3F51B5),
    checkedCheckmarkColor = Color(0xFFFFFFFF),
    uncheckedCheckmarkColor = Color(0xFFB0BEC5),
    checkedBoxColor = Color(0xFF40C4FF),
    uncheckedBoxColor = Color(0xFF424242),
    disabledCheckedBoxColor = Color(0xFF616161),
    disabledUncheckedBoxColor = Color(0xFF424242),
    disabledIndeterminateBoxColor = Color(0xFF616161),
    checkedBorderColor = Color(0xFF40C4FF),
    uncheckedBorderColor = Color(0xFFB0BEC5),
    disabledBorderColor = Color(0xFF616161),
    disabledUncheckedBorderColor = Color(0xFF424242),
    disabledIndeterminateBorderColor = Color(0xFF616161),
    highlightDefault = Color(0xFFE0E0E0),
    editBottomColor = Color(0xFF66BB6A),
    unActiveBottomColor = Color(0xFFFFFFFF),
    inputBorder = Color(0xFFFFFFFF),
)
private val light = CustomColor(
    topAppBarColor = Color.Transparent,
    bottomBarColor = Color(0xFFF5F5F5),
    listBottomBarColor = Color(0xFF757575),
    activeBottomColor = Color(0xFF2196F3),
    dangerColor = Color(0xFFFF5252),
    errorColor = Color(0xFFB71C1C),
    successColor = Color(0xFF43A047),
    iconColor = Color(0xFF0277BD),
    textTopBarColor = Color(0xFF2196F3),
    floatingColorContent = Color(0xFF303F9F),
    checkedCheckmarkColor = Color(0xFFFFFFFF),
    uncheckedCheckmarkColor = Color(0xFF757575),
    checkedBoxColor = Color(0xFF43A047),
    uncheckedBoxColor = Color(0xFFE0E0E0),
    disabledCheckedBoxColor = Color(0xFFBDBDBD),
    disabledUncheckedBoxColor = Color(0xFFEEEEEE),
    disabledIndeterminateBoxColor = Color(0xFFBDBDBD),
    checkedBorderColor = Color(0xFF43A047),
    uncheckedBorderColor = Color(0xFF9E9E9E),
    disabledBorderColor = Color(0xFFBDBDBD),
    disabledUncheckedBorderColor = Color(0xFFEEEEEE),
    disabledIndeterminateBorderColor = Color(0xFFBDBDBD),
    highlightDefault = Color(0xFF607D8B),
    editBottomColor = Color(0xFF388E3C),
    unActiveBottomColor = Color(0xFF363636),
    inputBorder  = Color(0xFF607D8B)
)


@Composable
fun TodoList2Theme(
    context: Context,
    content: @Composable () -> Unit,
) {
    val theme = DataStoreLocal(context)
    val state = theme.getTheme().collectAsState(initial = "System")

    val colorScheme = when (state.value) {
        "System" -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        "Dark" -> DarkColorScheme
        "Light" -> LightColorScheme
        else -> DarkColorScheme
    }

    val customColorScheme = when (state.value) {
        "System" -> if (isSystemInDarkTheme()) dark else light
        "Dark" -> dark
        "Light" -> light
        else -> dark
    }



    CompositionLocalProvider(
        LocalColors provides customColorScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}