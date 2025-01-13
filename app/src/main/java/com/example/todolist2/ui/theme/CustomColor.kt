package com.example.todolist2.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColors = compositionLocalOf<CustomColor> {
    error("Provider is not set")
}

@Immutable
data class CustomColor(
    val topAppBarColor: Color,
    val bottomBarColor: Color,
    val listBottomBarColor: Color,
    val activeBottomColor: Color,
    val dangerColor: Color,
    val errorColor: Color,
    val successColor: Color,
    val editBottomColor: Color,
    val iconColor: Color,
    val textTopBarColor: Color,
    val floatingColorContent: Color,
    val checkedCheckmarkColor: Color,
    val uncheckedCheckmarkColor: Color,
    val checkedBoxColor: Color,
    val uncheckedBoxColor: Color,
    val disabledCheckedBoxColor: Color,
    val disabledUncheckedBoxColor: Color,
    val disabledIndeterminateBoxColor: Color,
    val checkedBorderColor: Color,
    val uncheckedBorderColor: Color,
    val disabledBorderColor: Color,
    val disabledUncheckedBorderColor: Color,
    val disabledIndeterminateBorderColor: Color,
    val highlightDefault: Color,
    val unActiveBottomColor: Color,
    val inputBorder: Color,

    )