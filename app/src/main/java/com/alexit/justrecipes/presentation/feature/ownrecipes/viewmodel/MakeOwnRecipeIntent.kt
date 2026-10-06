package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

sealed class MakeOwnRecipeIntent {
    data class GetNameRecipe(val name: String) : MakeOwnRecipeIntent()
    data object SaveOwnRecipe : MakeOwnRecipeIntent()
}