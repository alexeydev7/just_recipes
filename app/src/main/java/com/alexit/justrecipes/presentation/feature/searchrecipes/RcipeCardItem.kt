package com.alexit.justrecipes.presentation.feature.searchrecipes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.alexit.justrecipes.R
import com.alexit.justrecipes.domain.model.database.RecipeCardModel
import com.alexit.justrecipes.presentation.components.CustomDivider
import com.alexit.justrecipes.presentation.components.dpToPx
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun RecipeCardItem (
    recipe: RecipeCardModel,
    onRecipeClick: (Int) -> Unit
){
    val padding = JustRecipesTheme.dimensions.gap1
    val widthRecipeCard = JustRecipesTheme.dimensions.widthRecipeCard
    val heightRecipeCard = JustRecipesTheme.dimensions.heightRecipeCard
    val backgroundRecipeCard = JustRecipesTheme.colors.background2
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val borderColor = JustRecipesTheme.colors.border2
    val borderThickness = JustRecipesTheme.dimensions.borderThickness
    val widthNameRecipeCard = JustRecipesTheme.dimensions.widthNameRecipeCard
    val heightNameRecipeCard = JustRecipesTheme.dimensions.heightNameRecipeCard
    val textNameStyle = JustRecipesTheme.typography.text1
    val colorText = JustRecipesTheme.colors.text4
    val sizeImage = JustRecipesTheme.dimensions.sizeImageRecipeCard
    val sizeIcon = JustRecipesTheme.dimensions.sizeIcon3
    val textIconStyle = JustRecipesTheme.typography.text5
    val colorIconOk = JustRecipesTheme.colors.iconOk
    val widthIconInfo = JustRecipesTheme.dimensions.widthIconInfoRecipeCard
    val imageAi = R.drawable.psychology_24px
    val imageOwn = R.drawable.note_stack_24px

    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (recipe.image != "ai" && recipe.image != "own" ) {
            bitmapState =
                BitmapFactory.decodeStream(context.assets.open("recipeimg/${recipe.image}"))
        }
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .size(
                width = widthRecipeCard,
                height = heightRecipeCard
            )
            .background(
                color = backgroundRecipeCard,
                shape = RoundedCornerShape(
                    size = radiusShape
                )
            )
            .border(
                border = BorderStroke(
                    width = borderThickness,
                    color = borderColor
                ),
                shape = RoundedCornerShape(
                    size = radiusShape,
                )
            )
            .clip(RoundedCornerShape(radiusShape))
            .clickable(
                enabled = true,
                onClick = { onRecipeClick(recipe.id) }
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(start = padding, end = padding)
                .height(heightNameRecipeCard)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                modifier = Modifier
                    .padding(start = padding)
                    .width(widthNameRecipeCard),
                style = textNameStyle,
                color = { colorText },
                text = recipe.name
            )

            if (recipe.image == "ai") {
                Image(
                    modifier = Modifier
                        .padding(end = padding)
                        .size(sizeImage)
                        .clip(
                            shape = RoundedCornerShape(
                                size = radiusShape
                            )
                        ),
                    imageVector = ImageVector.vectorResource(imageAi),
                    contentDescription = recipe.name,
                    colorFilter = ColorFilter.tint(borderColor)
                )
            }
            if (recipe.image == "own") {
                Image(
                    modifier = Modifier
                        .padding(end = padding)
                        .size(sizeImage)
                        .clip(
                            shape = RoundedCornerShape(
                                size = radiusShape
                            )
                        ),
                    imageVector = ImageVector.vectorResource(imageOwn),
                    contentDescription = recipe.name,
                    colorFilter = ColorFilter.tint(borderColor)
                )
            }
            if (bitmapState != null) {
                val bitmap = bitmapState!!.asImageBitmap()
                Image(
                    modifier = Modifier
                        .padding(end = padding)
                        .size(sizeImage)
                        .clip(
                            shape = RoundedCornerShape(
                                size = radiusShape
                            )
                        ),
                    bitmap = bitmap,
                    contentScale = ContentScale.Crop,
                    contentDescription = recipe.name
                )
            }
        }
        CustomDivider(
            color = borderColor,
            thickness = borderThickness.dpToPx(),
            startX = padding.dpToPx(),
            endX = (widthRecipeCard - padding * 2).dpToPx(),
            startY = 0f,
            endY = 0f
        )
        Row(
            modifier = Modifier
                .padding(start = padding, end = padding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .width(widthIconInfo),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (recipe.duration != null) {
                    Image(
                        modifier = Modifier
                            .size(sizeIcon),
                        imageVector = ImageVector.vectorResource(id = R.drawable.nest_clock_farsight_analog_24px),
                        contentDescription = stringResource(id = R.string.icon_duration),
                        colorFilter = ColorFilter.tint(colorText)
                    )
                    BasicText(
                        style = textIconStyle,
                        color = { colorText },
                        text = "${recipe.duration} ${stringResource(R.string.minute)}"
                    )
                }
            }
            Row(
                modifier = Modifier
                    .width(widthIconInfo),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (recipe.portion != null) {
                    Image(
                        modifier = Modifier
                            .size(sizeIcon),
                        imageVector = ImageVector.vectorResource(id = R.drawable.group_24px),
                        contentDescription = stringResource(id = R.string.icon_portion),
                        colorFilter = ColorFilter.tint(colorText)
                    )
                    BasicText(
                        style = textIconStyle,
                        color = { colorText },
                        text = "${recipe.portion} ${stringResource(R.string.person)}"
                    )
                }
            }
            Row(
                modifier = Modifier.width(widthIconInfo),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (recipe.ingredientsNo == 0) {
                    Image(
                        modifier = Modifier.size(sizeIcon),
                        imageVector = ImageVector.vectorResource(id = R.drawable.data_check_24px),
                        contentDescription = stringResource(id = R.string.icon_ingredient_data_ok),
                        colorFilter = ColorFilter.tint(colorIconOk)
                    )
                } else {
                    Image(
                        modifier = Modifier.size(sizeIcon),
                        imageVector = ImageVector.vectorResource(id = R.drawable.data_info_alert_24px),
                        contentDescription = stringResource(id = R.string.icon_ingredient_data_not),
                        colorFilter = ColorFilter.tint(
                            color = if (recipe.ingredientsOk > 0) colorIconOk
                            else colorText
                        )
                    )
                    BasicText(
                        style = textIconStyle,
                        color = { colorText },
                        text = "${recipe.ingredientsNo} ${stringResource(R.string.ingredient)}"
                    )
                }
            }
            Row(
                modifier = Modifier.width(widthIconInfo),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (recipe.isHealthy) {
                    Image(
                        modifier = Modifier.size(sizeIcon),
                        imageVector = ImageVector.vectorResource(id = R.drawable.nest_eco_leaf_24px),
                        contentDescription = stringResource(id = R.string.icon_healthy_eating),
                        colorFilter = ColorFilter.tint(colorIconOk)
                    )
                }
            }
        }
    }
}

