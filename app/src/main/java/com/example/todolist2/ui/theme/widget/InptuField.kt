package com.example.todolist2.ui.theme.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.todolist2.ui.theme.LocalColors


@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    decorator: TextFieldDecorator? = null,
    isError: Boolean = false,
    scrollState: ScrollState = rememberScrollState(),
) {
    val state = rememberTextFieldState(initialText = value)
    onValueChange(state.text as String)
    Box(
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = 0.5.dp,
                    color = if (isError) LocalColors.current.errorColor else LocalColors.current.inputBorder,
                ),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column {
            Box(
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
            ) {
                label()
            }
            BasicTextField(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .wrapContentHeight(),
                enabled = enabled,
                readOnly = readOnly,
                inputTransformation = inputTransformation,
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.tertiary),
                keyboardOptions = keyboardOptions,
                onKeyboardAction = onKeyboardAction,
                lineLimits = lineLimits,
                onTextLayout = onTextLayout,
                interactionSource = interactionSource,
                outputTransformation = outputTransformation,
                decorator = decorator,
                scrollState = scrollState,
            )
        }
    }
}

