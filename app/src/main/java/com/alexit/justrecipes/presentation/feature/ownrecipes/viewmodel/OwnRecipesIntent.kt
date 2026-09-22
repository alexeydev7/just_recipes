package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel

sealed class OwnRecipesIntent {
    data class IsRemoveOwnRecipe(val recipe: RecipeIdNameModel) : OwnRecipesIntent()
    //data object RemoveOwnRecipe : OwnRecipesIntent()
    //data object DismissRemoveOwnRecipe : OwnRecipesIntent()
    data class EditRecipe(val recipe: RecipeIdNameModel) : OwnRecipesIntent()
    data class ViewRecipe(val recipe: RecipeIdNameModel) : OwnRecipesIntent()
}