package com.alexit.justrecipes.presentation.feature.ownrecipes

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.alexit.justrecipes.R
import com.alexit.justrecipes.domain.model.database.HealthyFoodModel
import com.alexit.justrecipes.domain.model.database.IngredientEnergyModel
import com.alexit.justrecipes.domain.model.database.IngredientOwnRecipeModel
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import com.alexit.justrecipes.utility.getHealthyFoodData
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ShowIngredientsOwnRecipe(ingredients: PersistentList<IngredientOwnRecipeModel>, portion: Int) {
    val widthIngredientName = JustRecipesTheme.dimensions.widthIngredientNameInRecipe
    val contentPadding = JustRecipesTheme.dimensions.paddingFieldInput
    val styleText = JustRecipesTheme.typography.text1
    val colorText = JustRecipesTheme.colors.text4
    val styleStrEnergy = JustRecipesTheme.typography.text7
    val colorStrEnergy = JustRecipesTheme.colors.text6
    val colorDigitalDataField = JustRecipesTheme.colors.background4
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val liquid = stringArrayResource(R.array.liquid_foodstuff)
    val piece = stringArrayResource(R.array.piece_foodstuff)
    var healthyFoodData: HealthyFoodModel by remember {
        mutableStateOf(
            HealthyFoodModel(
                -1.0,
                -1.0,
                -1.0,
                -1.0
            )
        )
    }

    LaunchedEffect(ingredients) {
        withContext(Dispatchers.Default) {
            healthyFoodData =
                if (ingredients.any {
                        it.weight == null || it.energy < 0 || it.protein < 0 || it.fat < 0 || it.carbohydrate < 0
                    }) HealthyFoodModel(-1.0, -1.0, -1.0, -1.0)
                else {
                    getHealthyFoodData(ingredients.map {
                        IngredientEnergyModel(
                            id = it.id,
                            energy = it.energy,
                            protein = it.protein,
                            fat = it.fat,
                            carbohydrate = it.carbohydrate,
                            quantity = it.weight!!.toDouble(),
                            density = 1.0,
                        )
                    })
                }
        }
    }

    ingredients.forEach { ingredient ->
        val unit: String = if (liquid.contains(ingredient.category)) {
            stringResource(R.string.ml)
        } else if (piece.contains(ingredient.category)) {
            stringResource(R.string.piece)
        } else {
            stringResource(R.string.g)
        }
        Row(
            modifier = Modifier
                //.padding(padding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                modifier = Modifier
                    .width(widthIngredientName),
                text = ingredient.name.replaceFirstChar { it.uppercase() },
                style = styleText.copy(textAlign = TextAlign.Left),
                color = { colorText },
            )
            if (ingredient.weight != null) {
                BasicText(
                    text = "${ingredient.weight} $unit",
                    style = styleText.copy(textAlign = TextAlign.Right),
                    color = { colorText }
                )
            }
        }
    }
    if (healthyFoodData.energy > 0 && portion != 0) {
        val energyStr = stringResource(R.string.for_one_portion)
        BasicText(
            text = energyStr,
            style = styleStrEnergy,
            color = { colorStrEnergy },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorDigitalDataField,
                    shape = RoundedCornerShape(radiusShape)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val energyStr = stringResource(R.string.energy)
            val proteinStr = stringResource(R.string.protein)
            val fatStr = stringResource(R.string.fat)
            val carbohydrateStr = stringResource(R.string.carbohydrate)
            BasicText(
                modifier = Modifier
                    .padding(start = contentPadding),
                text = "$energyStr\n$proteinStr\n$fatStr\n$carbohydrateStr",
                style = styleText,
                color = { colorText },
            )
            val df = DecimalFormat("#.#")
            val energy = df.format(healthyFoodData.energy / portion)
            val protein = df.format(healthyFoodData.protein / portion)
            val fat = df.format(healthyFoodData.fat / portion)
            val carbohydrate = df.format(healthyFoodData.carbohydrate / portion)
            val kcal = stringResource(R.string.kcal)
            val g = stringResource(R.string.g)
            BasicText(
                modifier = Modifier
                    .padding(end = contentPadding),
                text = "$energy $kcal\n$protein $g\n$fat $g\n$carbohydrate $g",
                style = styleText,
                color = { colorText },
            )
        }
    }
}
