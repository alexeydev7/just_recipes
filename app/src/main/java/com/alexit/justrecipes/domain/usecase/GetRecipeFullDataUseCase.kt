package com.alexit.justrecipes.domain.usecase

import com.alexit.justrecipes.domain.model.database.HealthyFoodModel
import com.alexit.justrecipes.domain.model.database.IngredientDataModel
import com.alexit.justrecipes.domain.model.database.IngredientEnergyModel
import com.alexit.justrecipes.domain.model.database.IngredientFullModel
import com.alexit.justrecipes.domain.model.database.RecipeDataModel
import com.alexit.justrecipes.domain.model.database.RecipeFullModel
import com.alexit.justrecipes.domain.repository.RecipesRepository
import com.alexit.justrecipes.utility.getHealthyFoodData
import javax.inject.Inject
import kotlin.Int

class GetRecipeFullDataUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    suspend operator fun invoke(recipeId: Int): RecipeFullModel {
        val recipeData: RecipeDataModel = recipesRepository.getRecipeData(recipeId)
        val ingredientsFullData: List<IngredientFullModel> = recipesRepository.getIngredientsData(recipeId)
        val healthyFoodData: HealthyFoodModel = if (ingredientsFullData.any { it.energy < 0 ||
                    it.protein < 0 ||
                    it.fat < 0 ||
                    it.carbohydrate < 0 ||
                    it.quantity < 0
            }) HealthyFoodModel(-1.0, -1.0, -1.0, -1.0)
        else {
            getHealthyFoodData(ingredientsFullData.map {
                IngredientEnergyModel(
                    id = it.id,
                    energy = it.energy,
                    protein = it.protein,
                    fat = it.fat,
                    carbohydrate = it.carbohydrate,
                    quantity = it.quantity,
                    density = it.density,
                )
            })
        }
        val recipe = RecipeFullModel (
            id = recipeData.id,
            name = recipeData.name,
            image = recipeData.image,
            portion = recipeData.portion,
            duration = recipeData.duration,
            energy = healthyFoodData.energy ,
            protein = healthyFoodData.protein,
            fat = healthyFoodData.fat,
            carbohydrate = healthyFoodData.carbohydrate,
            details = recipeData.details,
            detailsImage = recipeData.detailsImage,
            ingredients = ingredientsFullData.map {
                IngredientDataModel(
                    id = it.id,
                    name = it.name,
                    quantity = it.quantity,
                    unit = it.unit,
                    density = it.density,
                    weight = it.weight,
                    isSynonym = it.isSynonym
                )
            }
        )
        return recipe
    }
}