package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel

data class OwnRecipesUiState(
    val isDeleteRecipe: Boolean = false,
    val deletingRecipe: RecipeIdNameModel = RecipeIdNameModel(-1, ""),
)
