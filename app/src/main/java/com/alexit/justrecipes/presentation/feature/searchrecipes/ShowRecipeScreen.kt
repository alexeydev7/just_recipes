package com.alexit.justrecipes.presentation.feature.searchrecipes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.domain.model.database.RecipeFullModel
import com.alexit.justrecipes.presentation.components.CircleLoader
import com.alexit.justrecipes.presentation.components.CustomDivider
import com.alexit.justrecipes.presentation.components.CustomPopup
import com.alexit.justrecipes.common.NotifySideEffect
import com.alexit.justrecipes.presentation.components.TitlePanel
import com.alexit.justrecipes.utility.dpToPx
import com.alexit.justrecipes.presentation.feature.searchrecipes.viewmodel.ShowRecipeViewModel
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ShowRecipeScreen(
    showRecipeViewModel: ShowRecipeViewModel = hiltViewModel(),
    recipeId: Int,
    onBackClick: () -> Unit
) {
    val recipeNullable by showRecipeViewModel.recipeState

    val scrollState = rememberScrollState()

    val colorText = JustRecipesTheme.colors.text4
    val styleName = JustRecipesTheme.typography.text6
    val colorAdding = JustRecipesTheme.colors.text6
    val styleAdding = JustRecipesTheme.typography.text7
    val styleText = JustRecipesTheme.typography.text1
    val styleEnergy = JustRecipesTheme.typography.text8
    val padding = JustRecipesTheme.dimensions.gap1
    val colorInputtedIngredients = JustRecipesTheme.colors.inputtedIngredients
    val widthIngredientName = JustRecipesTheme.dimensions.widthIngredientNameInRecipe

    var isNewNotify by remember { mutableStateOf(false) }
    var notifyMessage by remember { mutableStateOf("") }
    var notifyState by remember { mutableStateOf(NotifyState.INFO) }
    val context = LocalContext.current

    LaunchedEffect(recipeId) {
        showRecipeViewModel.getRecipe(recipeId)
        showRecipeViewModel.sideEffect.collectLatest { notify ->
            when (notify) {
                is NotifySideEffect.ShowNotify -> {
                    isNewNotify = true
                    notifyMessage = "${notify.message.asString(context)}\n${notify.addition}".trimEnd()
                    notifyState = notify.state
                }
            }
        }
    }

    if (isNewNotify) {
        CustomPopup(
            message = notifyMessage,
            state = notifyState,
            onDismissRequest = { isNewNotify = !isNewNotify }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TitlePanel(
            text = stringResource(R.string.title_show_recipe),
            onLeftClick = onBackClick,
            textLeft = stringResource(R.string.go_back)
        )
        if (recipeNullable == null) LoadingScreen()
        else {
            val recipe: RecipeFullModel = recipeNullable!!
            //-----Name-----
            BasicText(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally),
                text = recipe.name,
                style = styleName,
                color = { colorText },
            )
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //-----Image-----
                if (recipe.image != "ai" && recipe.image != "own") {
                    ImageShow(imageName = recipe.image)
                }
                Divider()

                //-----Energy-----
                if (recipe.energy >= 0 ||
                    recipe.protein >= 0 ||
                    recipe.fat >= 0 ||
                    recipe.carbohydrate >= 0
                ) {
                    BasicText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = padding, top = padding)
                            .align(Alignment.Start),
                        text = stringResource(R.string.for_one_portion),
                        style = styleAdding,
                        color = { colorAdding },
                    )

                    Row(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val energyStr = stringResource(R.string.energy)
                        val proteinStr = stringResource(R.string.protein)
                        val fatStr = stringResource(R.string.fat)
                        val carbohydrateStr = stringResource(R.string.carbohydrate)
                        BasicText(
                            text = "$energyStr\n$proteinStr\n$fatStr\n$carbohydrateStr",
                            style = styleText,
                            color = { colorText },
                        )
                        val portion: Int = recipe.portion ?: 1
                        val df = DecimalFormat("#.#")
                        val energy = df.format(recipe.energy / portion)
                        val protein = df.format(recipe.protein / portion)
                        val fat = df.format(recipe.fat / portion)
                        val carbohydrate = df.format(recipe.carbohydrate / portion)
                        val kcal = stringResource(R.string.kcal)
                        val g = stringResource(R.string.g)
                        BasicText(
                            text = "$energy $kcal\n$protein $g\n$fat $g\n$carbohydrate $g",
                            style = styleEnergy,
                            color = { colorText },
                        )
                    }
                    Divider()
                }

                if (recipe.portion != null) {
                    Row(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val portionStr = stringResource(R.string.portion)
                        val portion = recipe.portion
                        val persons = stringResource(R.string.person)
                        BasicText(
                            text = portionStr,
                            style = styleText,
                            color = { colorText },
                        )
                        BasicText(
                            text = "$portion $persons",
                            style = styleEnergy,
                            color = { colorText },
                        )
                    }
                }

                //-----Duration-----
                if (recipe.duration != null) {
                    Row(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val durationStr = stringResource(R.string.duration)
                        val duration = recipe.duration
                        val minute = stringResource(R.string.minute)
                        BasicText(
                            text = durationStr,
                            style = styleText,
                            color = { colorText },
                        )
                        BasicText(
                            text = "$duration $minute",
                            style = styleEnergy,
                            color = { colorText },
                        )
                    }
                }
                Divider()

                //-----Ingredients-----
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val ingredientsStr = stringResource(R.string.ingredients)
                    BasicText(
                        text = ingredientsStr,
                        style = styleEnergy,
                        color = { colorText },
                    )
                    recipe.ingredients.forEach { ingredient ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val colorName =
                                if (ingredient.isSynonym > 0) colorInputtedIngredients else colorText
                            BasicText(
                                modifier = Modifier
                                    .width(widthIngredientName),
                                text = ingredient.name.replaceFirstChar { it.uppercase() },
                                style = styleText.copy(textAlign = TextAlign.Left),
                                color = { colorName },
                            )
                            val df = DecimalFormat("#.##")
                            val amount: Double = if (ingredient.quantity >= 0 && ingredient.density >= 0) {
                                ingredient.quantity * ingredient.density
                            } else -1.0
                            val colorAmount =
                                if (ingredient.weight != null && ingredient.weight >= amount) colorInputtedIngredients
                                else colorText
                            val amountStr = when (amount) {
                                -1.0 -> ""
                                0.0 -> stringResource(R.string.to_taste)
                                else -> "${df.format(ingredient.quantity)} ${ingredient.unit}"
                            }
                            BasicText(
                                text = amountStr,
                                style = styleEnergy.copy(textAlign = TextAlign.Right),
                                color = { colorAmount },
                            )
                        }
                    }
                }

                Divider()

                //-----Steps-----
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val stepsText: List<String> = recipe.details?.split("(^_^)") ?: emptyList()
                    val stepsImg: List<String> = recipe.detailsImage?.split(";") ?: emptyList()
                    val maxSize = maxOf(stepsText.size, stepsImg.size)

                    for (i in 0 until maxSize) {
                        val stepText = stepsText.getOrNull(i)
                        val stepImg = stepsImg.getOrNull(i)
                        if (stepText != null) {
                            BasicText(
                                modifier = Modifier.padding(top = padding),
                                text = "${i + 1}. $stepText",
                                style = styleText.copy(textAlign = TextAlign.Justify),
                                color = { colorText },
                            )
                        }
                        if (stepImg != null) {
                            ImageShow(imageName = stepImg)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    var isLoading by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircleLoader(
            color = JustRecipesTheme.colors.circleLoader,
            modifier = Modifier.size(JustRecipesTheme.dimensions.sizeCircleLoader),
            isVisible = isLoading
        )
    }
}

@Composable
private fun Divider() {
    val dividerColor = JustRecipesTheme.colors.border2
    val dividerThickness = JustRecipesTheme.dimensions.borderThickness
    val dividerWidth = JustRecipesTheme.dimensions.widthInputTextField
    CustomDivider(
        color = dividerColor,
        thickness = dividerThickness.dpToPx(),
        startX = - (dividerWidth / 2).dpToPx(),
        endX = (dividerWidth / 2).dpToPx(),
        startY = 0f,
        endY = 0f
    )
}

@Composable
fun ImageShow(imageName: String) {
    val padding = JustRecipesTheme.dimensions.gap1
    val roundedCorner = JustRecipesTheme.dimensions.radiusCornerField
    val sizeImage = JustRecipesTheme.dimensions.sizeImageRecipe

    Box(
        modifier = Modifier
            .padding(top = padding, bottom = padding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            bitmapState =
                BitmapFactory.decodeStream(context.assets.open("recipeimg/${imageName}"))
        }

        if (bitmapState != null) {
            val bitmap = bitmapState!!.asImageBitmap()
            Image(
                modifier = Modifier
                    .padding(end = padding)
                    .size(sizeImage)
                    .clip(
                        RoundedCornerShape(
                            roundedCorner
                        )
                    ),
                bitmap = bitmap,
                contentScale = ContentScale.Crop,
                contentDescription = imageName
            )
        }
    }
}