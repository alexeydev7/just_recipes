package com.alexit.justrecipes.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.alexit.justrecipes.domain.model.database.IngredientEnergyModel
import com.alexit.justrecipes.domain.model.database.RecipeCardModel
import com.alexit.justrecipes.domain.repository.RecipesRepository
import com.alexit.justrecipes.utility.getHealthyFoodData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val LIMIT_ENERGY = 600
const val RATIO_CARBO_FAT_PROTEIN = 4.0

class GetRecipeCardDataUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(query: String): Flow<PagingData<RecipeCardModel>> {
    val recipesFlow: Flow<PagingData<RecipeCardModel>> =
        recipesRepository.getRecipesCardData(query).map { recipesData ->
            recipesData.map {
                val ingredientsEnergyData = recipesRepository.getIngredientsEnergy(it.id)
                RecipeCardModel(
                    id = it.id,
                    name = it.name,
                    image = it.image,
                    portion = it.portion,
                    duration = it.duration,
                    ingredientsOk = it.ingredientsOk,
                    ingredientsNo = it.ingredientsNo,
                    isHealthy = isHealthy(ingredientsEnergyData, it.portion)
                )
            }
        }
    return recipesFlow
    }
}

private fun isHealthy(ingredients: List<IngredientEnergyModel>, portion: Int?): Boolean {
    if (ingredients.any { it.energy < 0 ||
                it.protein < 0 ||
                it.fat < 0 ||
                it.carbohydrate < 0 ||
                it.quantity < 0
    }) return false

    val healthyFoodData = getHealthyFoodData(ingredients)
    val portionOk: Int = portion ?: 1
    val isHealthy =
        healthyFoodData.carbohydrate / healthyFoodData.fat > RATIO_CARBO_FAT_PROTEIN &&
                healthyFoodData.carbohydrate / healthyFoodData.protein > RATIO_CARBO_FAT_PROTEIN &&
                healthyFoodData.energy / portionOk <= LIMIT_ENERGY
    return isHealthy
}

