package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel

sealed class OwnRecipesIntent {
    data class IsDeleteOwnRecipe(val recipe: RecipeIdNameModel) : OwnRecipesIntent()
    data object DeleteOwnRecipe : OwnRecipesIntent()
    data object DismissDeleteOwnRecipe : OwnRecipesIntent()
    data class EditRecipe(val recipe: RecipeIdNameModel) : OwnRecipesIntent()
    data class ViewRecipe(val recipe: RecipeIdNameModel) : OwnRecipesIntent()
}