package com.alexit.justrecipes.domain.usecase

import com.alexit.justrecipes.domain.repository.RecipesRepository
import javax.inject.Inject

class DeleteOwnRecipeUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    suspend operator fun invoke(recipeId: Int) {
        recipesRepository.deleteOwnRecipe(recipeId)
    }
}