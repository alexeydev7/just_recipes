package com.alexit.justrecipes.domain.usecase

import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.domain.model.database.IngredientOwnRecipeModel
import com.alexit.justrecipes.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIngredientsOwnRecipeUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(): Flow<SourceState<List<IngredientOwnRecipeModel>>> =
        recipesRepository.getIngredientsOwnRecipe()
}
