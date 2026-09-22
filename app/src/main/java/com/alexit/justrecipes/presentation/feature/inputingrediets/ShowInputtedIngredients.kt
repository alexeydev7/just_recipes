package com.alexit.justrecipes.presentation.feature.inputingrediets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.alexit.justrecipes.R
import com.alexit.justrecipes.domain.model.database.IngredientInputedModel
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.collections.immutable.PersistentList

@Composable
fun ShowInputtedIngredients(
    inputtedIngredients: PersistentList<IngredientInputedModel>,
    onDeleteClick: (IngredientInputedModel) -> Unit,
    onWeightClick: (Int, Int, String) -> Unit
) {
    val iconDeleteIngredient = R.drawable.round_do_not_disturb_on_24
    val descriptionIconDeleteIngredient = R.string.delete_item
    val colorIconDeleteIngredient = JustRecipesTheme.colors.iconDeleteIngredient
    val colorInputtedIngredientsField = JustRecipesTheme.colors.background4
    val colorInputtedIngredientText = JustRecipesTheme.colors.text4
    val textStyleInputtedIngredient = JustRecipesTheme.typography.text1
    val iconScale = R.drawable.outline_scale_24
    val descriptionIconScale = R.string.icon_scale
    val colorBackgroundWeightIngredient = JustRecipesTheme.colors.background2
    val colorWeightIngredient = JustRecipesTheme.colors.text5
    val textStyleWeightIngredient = JustRecipesTheme.typography.text3
    val contentPadding = JustRecipesTheme.dimensions.contentPaddingField
    val width = JustRecipesTheme.dimensions.widthInputTextField
    val bottomMenuHeight = JustRecipesTheme.dimensions.heightBottomMenu
    val widthInputtedIngredientField = JustRecipesTheme.dimensions.widthShowedTextField
    val widthInputtedIngredientText = JustRecipesTheme.dimensions.widthShowedText
    val widthInputtedIngredientWeight = JustRecipesTheme.dimensions.widthInputtedIngredientWeight
    val heightInputtedIngredientWeight = JustRecipesTheme.dimensions.heightInputtedIngredientWeight
    val sizeIcon = JustRecipesTheme.dimensions.sizeIcon1
    val sizeIconScale = JustRecipesTheme.dimensions.sizeIcon2
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField

    val keyboardController = LocalSoftwareKeyboardController.current
    val liquid = stringArrayResource(R.array.liquid_foodstuff)
    val piece = stringArrayResource(R.array.piece_foodstuff)
    LazyColumn(
        modifier = Modifier
            .consumeWindowInsets(paddingValues = PaddingValues(bottomMenuHeight))
            .imePadding()
            .animateContentSize()
    ) {
        items(items = inputtedIngredients, key = { it.id }) { ingredient ->
            val state = rememberTextFieldState(ingredient.weight?.toString() ?: "")
            var isFocusedWeight by remember { mutableStateOf(false) }
            val unit: String = if (liquid.contains(ingredient.category)) {
                stringResource(R.string.ml)
            } else if (piece.contains(ingredient.category)) {
                stringResource(R.string.piece)
            } else {
                stringResource(R.string.g)
            }
            Row(
                modifier = Modifier
                    .width(width)
                    .padding(top = contentPadding)
                    .animateItem(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier
                        .size(sizeIcon)
                        .clip(RoundedCornerShape(radiusShape))
                        .clickable(
                            enabled = true,
                            onClick = {
                                onDeleteClick(ingredient)
                                state.clearText()
                            }
                        ),
                    imageVector = ImageVector.vectorResource(iconDeleteIngredient),
                    contentDescription = stringResource(descriptionIconDeleteIngredient),
                    colorFilter = ColorFilter.tint(colorIconDeleteIngredient)
                )
                BasicTextField(
                    state = state,
                    modifier = Modifier
                        .onFocusChanged { isFocusedWeight = it.isFocused }
                        .width(widthInputtedIngredientField)
                        .wrapContentHeight()
                        .background(
                            color = colorInputtedIngredientsField,
                            shape = RoundedCornerShape(radiusShape)
                        )
                        .padding(contentPadding),
                    enabled = true,
                    inputTransformation = InputTransformation.maxLength(4).then {
                        if (asCharSequence().contains("\\D".toRegex())) revertAllChanges()
                        if (asCharSequence().matches("0+".toRegex())) revertAllChanges()
                    },
                    textStyle = textStyleWeightIngredient.copy(
                        textAlign = TextAlign.End,
                        color = colorWeightIngredient
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    onKeyboardAction = KeyboardActionHandler {
                        if (state.text.isNotEmpty()) {
                            onWeightClick(ingredient.id, state.text.toString().toInt(), ingredient.name)
                            keyboardController?.hide()
                        }
                    },
                    cursorBrush = Brush.verticalGradient(
                        0.00f to Color.Transparent,
                        0.20f to Color.Transparent,
                        0.20f to colorWeightIngredient,
                        0.80f to colorWeightIngredient,
                        0.80f to Color.Transparent,
                        1.00f to Color.Transparent
                    ),
                    decorator = { innerTextField ->
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BasicText(
                                modifier = Modifier
                                    .width(widthInputtedIngredientText),
                                style = textStyleInputtedIngredient,
                                color = { colorInputtedIngredientText },
                                text = ingredient.name
                            )
                            if (isFocusedWeight || state.text.isNotEmpty()){
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if (isFocusedWeight) colorBackgroundWeightIngredient else colorInputtedIngredientsField,
                                            shape = RoundedCornerShape(size = radiusShape / 2)
                                        )
                                        .size(
                                            width = widthInputtedIngredientWeight,
                                            height = heightInputtedIngredientWeight
                                        )
                                        .padding(end = contentPadding),
                                    contentAlignment = Alignment.CenterEnd,

                                ) {
                                    innerTextField()
                                }
                                BasicText(
                                    style = textStyleWeightIngredient,
                                    color = { colorWeightIngredient },
                                    text = unit
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(
                                            width = widthInputtedIngredientWeight,
                                            height = heightInputtedIngredientWeight
                                        ),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(sizeIconScale),
                                        imageVector = ImageVector.vectorResource(iconScale),
                                        contentDescription = stringResource(descriptionIconScale),
                                        colorFilter = ColorFilter.tint(colorWeightIngredient)
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
