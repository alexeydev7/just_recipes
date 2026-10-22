package com.alexit.justrecipes.presentation.feature.inputingrediets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.alexit.justrecipes.R
import com.alexit.justrecipes.domain.model.database.CategoryModel
import com.alexit.justrecipes.presentation.components.CustomDivider
import com.alexit.justrecipes.utility.dpToPx
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.collections.immutable.PersistentList

@Composable
fun MakeNewIngredient(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    item: String,
    listCategory: PersistentList<CategoryModel>,
) {
    val heightDialog = JustRecipesTheme.dimensions.heightNewIngredientDialog
    val widthDialog = JustRecipesTheme.dimensions.widthNewIngredientDialog
    val colorBackground = JustRecipesTheme.colors.background4
    val colorStroke = JustRecipesTheme.colors.text4
    val colorText = JustRecipesTheme.colors.text4
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val borderThickness = JustRecipesTheme.dimensions.borderThickness
    val textDialogPre = stringResource(R.string.add_unknown_ingredient)
    val textDialogAft = stringResource(R.string.select_category_ingredient)
    val textStyle = JustRecipesTheme.typography.text1
    val textStyleCategory = JustRecipesTheme.typography.text2
    val textConfirmation = stringResource(R.string.confirmation)
    val textDismiss = stringResource(R.string.dismiss)
    val contentPadding = JustRecipesTheme.dimensions.contentPaddingField
    val colorBackgroundCategory = JustRecipesTheme.colors.background2
    val colorBorderCategory = JustRecipesTheme.colors.border2
    val colorTextCategory = JustRecipesTheme.colors.text2
    val colorBackgroundCategorySelected = JustRecipesTheme.colors.background3

    val selectedCategory = remember {  mutableStateOf("") }
    Dialog(onDismissRequest = onDismissRequest ) {
        Column(
            modifier = Modifier
                .size(
                    height = heightDialog,
                    width = widthDialog
                )
                .background(
                    color = colorBackground,
                    shape = RoundedCornerShape(size = radiusShape)
                )
                .border(
                    border = BorderStroke(
                        width = borderThickness,
                        color = colorStroke
                    ),
                    shape = RoundedCornerShape(size = radiusShape)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicText(
                style = textStyle.copy(
                    textAlign = TextAlign.Center
                ),
                color = { colorText },
                text = "$textDialogPre\n\r$item?\n\r$textDialogAft"
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(
                        fill = true,
                        weight = 1f
                    )
                    .background(
                        color = colorBackgroundCategory,
                        shape = RoundedCornerShape(size = radiusShape)
                    )
                    .border(
                        border = BorderStroke(
                            width = borderThickness,
                            color = colorBorderCategory
                        )
                    )
                    .padding(start = contentPadding, end = contentPadding)
                    .animateContentSize(),
                verticalArrangement = Arrangement.Center
            ) {
                items(items = listCategory, key = { it.id }) { category ->
                    BasicText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (category.category == selectedCategory.value)
                                    colorBackgroundCategorySelected
                                else colorBackgroundCategory
                            )
                            .selectable(
                                selected = category.category == selectedCategory.value,
                                onClick = {
                                    if (selectedCategory.value != category.category)
                                    { selectedCategory.value = category.category }
                                    else
                                    { selectedCategory.value = "" }
                                })
                            .padding(contentPadding),
                        text = category.category,
                        style = textStyleCategory,
                        color = { colorTextCategory }
                    )
                    CustomDivider(
                        color = colorTextCategory,
                        thickness = borderThickness.dpToPx(),
                        startX = 0f,
                        endX = (widthDialog - contentPadding * 2).dpToPx(),
                        startY = 0f,
                        endY = 0f
                    )
                }
            }
            Row(
                modifier = Modifier
                    .height(heightDialog / 10)
                    .border(
                        border = BorderStroke(
                            width = borderThickness,
                            color = colorStroke
                        ),
                        shape = RoundedCornerShape(
                            bottomStart = radiusShape,
                            bottomEnd = radiusShape
                        )
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = radiusShape))
                        .clickable(
                            enabled = true,
                            onClick = onDismissRequest
                        )
                        .width(widthDialog / 2)
                        .fillMaxHeight()
                        .wrapContentHeight(),
                    style = textStyle.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = { colorText },
                    text = textDismiss
                )
                CustomDivider(
                    color = colorTextCategory,
                    thickness = borderThickness.dpToPx(),
                    startX = 0f,
                    endX = 0f,
                    startY = ((heightDialog / 10) / 2).dpToPx(),
                    endY = - ((heightDialog / 10) / 2).dpToPx()
                )
                BasicText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = radiusShape))
                        .alpha( if (selectedCategory.value != "") 1f else 0.2f )
                        .clickable(
                            enabled = true,
                            onClick = {
                                if (selectedCategory.value != "") onConfirmation(selectedCategory.value)
                            }
                        )
                        .width(widthDialog / 2)
                        .fillMaxHeight()
                        .wrapContentHeight(),
                    style = textStyle.copy(
                        textAlign = TextAlign.Center
                    ),
                    color =  { colorText },
                    text = textConfirmation
                )
            }
        }
    }
}
