package com.alexit.justrecipes.domain.usecase

import com.alexit.justrecipes.domain.model.database.IngredientShortModel
import com.alexit.justrecipes.domain.repository.RecipesRepository
import javax.inject.Inject

class GetIngredientUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    suspend operator fun invoke(ingredientName: String): IngredientShortModel? {
        return recipesRepository.getIngredient(ingredientName)
    }
}