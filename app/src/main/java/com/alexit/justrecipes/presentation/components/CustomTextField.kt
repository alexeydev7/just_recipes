package com.alexit.justrecipes.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.alexit.justrecipes.R
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun CustomTextField (
    state: TextFieldState,
    onDoneClick: ((String) -> Unit)? = null,
    iconPlaceholder: Int,
    iconDescriptionPlaceholder: Int,
    placeholder: String,
) {
    val height = JustRecipesTheme.dimensions.heightInputTextField
    val width = JustRecipesTheme.dimensions.widthInputTextField
    val textStyle = JustRecipesTheme.typography.text2
    val focusedField = JustRecipesTheme.colors.background2
    val focusedBorderField = JustRecipesTheme.colors.border2
    val focusedTextColor = JustRecipesTheme.colors.text2
    val unfocusedField = JustRecipesTheme.colors.background3
    val unfocusedBorderField = JustRecipesTheme.colors.border3
    val unfocusedTextColor = JustRecipesTheme.colors.text3
    val contentPadding = JustRecipesTheme.dimensions.contentPaddingField
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val borderThickness = JustRecipesTheme.dimensions.borderThickness
    val sizeIcon = JustRecipesTheme.dimensions.sizeIcon1
    val colorIcon = JustRecipesTheme.colors.iconSearchIngredient

    var isFocused by remember { mutableStateOf(false) }

    val colorField: Color
    val colorBorderField: Color

    if (state.text.isNotEmpty() || isFocused) {
        colorField = focusedField
        colorBorderField = focusedBorderField
    } else {
        colorField = unfocusedField
        colorBorderField = unfocusedBorderField
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        state = state,
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
            .height(height)
            .width(width),
        enabled = true,
        inputTransformation = InputTransformation {
            if (asCharSequence().isNotEmpty() && !asCharSequence().first()
                    .isLetter()
            ) revertAllChanges()
        },
        textStyle = textStyle.copy(color = focusedTextColor),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = KeyboardActionHandler {
            if (state.text.isNotEmpty()) {
                if (onDoneClick != null) onDoneClick(state.text.toString().trim())
                keyboardController?.hide()
            }
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = Brush.verticalGradient(
            0.00f to Color.Transparent,
            0.25f to Color.Transparent,
            0.25f to focusedTextColor,
            0.80f to focusedTextColor,
            0.80f to Color.Transparent,
            1.00f to Color.Transparent
        ),
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(
                        color = colorField,
                        shape = RoundedCornerShape(size = radiusShape)
                    )
                    .border(
                        border = BorderStroke(
                            width = borderThickness,
                            color = colorBorderField
                        ),
                        shape = RoundedCornerShape(size = radiusShape)
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.text.isNotEmpty() || isFocused) {
                    Image(
                        modifier = Modifier
                            .size(sizeIcon)
                            .padding(start = contentPadding),
                        imageVector = ImageVector.vectorResource(id = R.drawable.search_24px),
                        contentDescription = stringResource(id = R.string.icon_search),
                        colorFilter = ColorFilter.tint(colorIcon)
                    )
                    Box(
                        modifier = Modifier
                            .size(
                                width = width - (sizeIcon * 2) - contentPadding,
                                height = height
                            )
                            .padding(start = contentPadding),
                        contentAlignment = Alignment.CenterStart
                    )
                    {
                        innerTextField()
                    }
                    Image(
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = { state.clearText() }
                            )
                            .size(sizeIcon)
                            .padding(start = contentPadding),
                        imageVector = ImageVector.vectorResource(id = R.drawable.close_24px),
                        contentDescription = stringResource(id = R.string.clear_text),
                        colorFilter = ColorFilter.tint(colorIcon)
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(sizeIcon)
                            .padding(start = contentPadding),
                        imageVector = ImageVector.vectorResource(id = iconPlaceholder),
                        contentDescription = stringResource(id = iconDescriptionPlaceholder),
                        colorFilter = ColorFilter.tint(unfocusedTextColor)
                    )
                    Box(
                        modifier = Modifier.padding(start = contentPadding)
                    ) {
                        BasicText(
                            style = textStyle,
                            text = placeholder,
                            color = { unfocusedTextColor }
                        )
                        innerTextField()
                    }
                }
            }
        }
    )
}
