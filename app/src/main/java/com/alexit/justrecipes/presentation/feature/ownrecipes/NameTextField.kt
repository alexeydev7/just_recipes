package com.alexit.justrecipes.presentation.feature.ownrecipes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.alexit.justrecipes.R
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun NameTextField (
    state: TextFieldState,
    onDoneClick: ((String) -> Unit),
    iconPlaceholder: Int,
    iconDescriptionPlaceholder: Int,
    placeholder: String,
) {
    val textStyle = JustRecipesTheme.typography.text10
    val textColor = JustRecipesTheme.colors.text7
    val sizeIcon = JustRecipesTheme.dimensions.sizeIcon2
    val colorIcon = JustRecipesTheme.colors.iconEdit
    val contentPadding = JustRecipesTheme.dimensions.paddingFieldInput

    var isFocused by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        state = state,
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused },
        enabled = true,
        inputTransformation = InputTransformation {
            if (asCharSequence().isNotEmpty() && !asCharSequence().first()
                    .isLetter()
            ) revertAllChanges()
        },
        textStyle = textStyle.copy(color = textColor),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = KeyboardActionHandler {
            if (state.text.isNotEmpty()) {
                onDoneClick(state.text.toString().trim())
                keyboardController?.hide()
            }
        },
        lineLimits = TextFieldLineLimits.MultiLine(),
        cursorBrush = SolidColor(textColor),
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = contentPadding)
                        .weight(1f),
                ) {
                    innerTextField()
                }
                if (state.text.isNotEmpty()) {
                    Image(
                        modifier = Modifier
                            .clip(RoundedCornerShape(sizeIcon))
                            .size(sizeIcon)
                            .clickable(
                                enabled = true,
                                onClick = { state.clearText() }
                            ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.close_24px),
                        contentDescription = stringResource(id = R.string.clear_text),
                        colorFilter = ColorFilter.tint(textColor),
                    )
                }
            }
            if (state.text.isEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        modifier = Modifier
                            .padding(end = contentPadding)
                            .weight(1f),
                        style = textStyle,
                        text = placeholder,
                        color = { textColor }
                    )
                    Image(
                        modifier = Modifier
                            .clip(RoundedCornerShape(sizeIcon))
                            .size(sizeIcon)
                            .clickable(
                                enabled = true,
                                onClick = { state.setTextAndPlaceCursorAtEnd(placeholder) }
                            ),
                        imageVector = ImageVector.vectorResource(id = iconPlaceholder),
                        contentDescription = stringResource(id = iconDescriptionPlaceholder),
                        colorFilter = ColorFilter.tint(colorIcon)
                    )
                }
            }
        }
    )
}