package com.alexit.justrecipes.domain.usecase

import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel
import com.alexit.justrecipes.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOwnRecipesIdNameUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(query: String): Flow<SourceState<List<RecipeIdNameModel>>> =
        recipesRepository.getOwnRecipesIdName(query)
}