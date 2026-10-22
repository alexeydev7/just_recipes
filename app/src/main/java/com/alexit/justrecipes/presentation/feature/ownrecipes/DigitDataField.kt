package com.alexit.justrecipes.presentation.feature.ownrecipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun DigitDataField(
    state: TextFieldState,
    text: String,
    onDoneClick: ((Int) -> Unit),
    placeholder: Int,
    unit: String
) {
    val colorBackgroundField = JustRecipesTheme.colors.background4
    val colorBackgroundInputField = JustRecipesTheme.colors.background2
    val colorInputDigit = JustRecipesTheme.colors.text5
    val textStyleInputDigit = JustRecipesTheme.typography.text3
    val widthInputDigit = JustRecipesTheme.dimensions.widthInputDigit
    val heightInputDigit = JustRecipesTheme.dimensions.heightInputDigit
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val textStyle = JustRecipesTheme.typography.text1
    val textColor = JustRecipesTheme.colors.text4
    val contentPadding = JustRecipesTheme.dimensions.contentPaddingField

    var isFocused by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        state = state,
        modifier = Modifier
            .padding(contentPadding)
            .onFocusChanged { isFocused = it.isFocused },
        enabled = true,
        inputTransformation = InputTransformation.maxLength(2).then {
            if (asCharSequence().contains("\\D".toRegex())) revertAllChanges()
            if (asCharSequence().matches("0+".toRegex())) revertAllChanges()
        },
        textStyle = textStyleInputDigit.copy(
            textAlign = TextAlign.End,
            color = colorInputDigit
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = KeyboardActionHandler {
            if (state.text.isNotEmpty()) {
                onDoneClick(state.text.toString().trim().toInt())
                keyboardController?.hide()
            }
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = Brush.verticalGradient(
            0.00f to Color.Transparent,
            0.20f to Color.Transparent,
            0.20f to textColor,
            0.80f to textColor,
            0.80f to Color.Transparent,
            1.00f to Color.Transparent,
        ),
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    modifier = Modifier
                        .weight(1f),
                    style = textStyle,
                    color = { textColor },
                    text = text
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isFocused) colorBackgroundInputField else colorBackgroundField,
                            shape = RoundedCornerShape(size = radiusShape / 2)
                        )
                        .size(
                            width = widthInputDigit,
                            height = heightInputDigit
                        )
                        .padding(end = contentPadding),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    if (state.text.isEmpty()) {
                        BasicText(
                            style = textStyleInputDigit,
                            text = "$placeholder",
                            color = { colorInputDigit }
                        )
                    } else {
                        innerTextField()
                    }
                }
                BasicText(
                    style = textStyle,
                    color = { textColor },
                    text = unit
                )
            }
        }
    )
}