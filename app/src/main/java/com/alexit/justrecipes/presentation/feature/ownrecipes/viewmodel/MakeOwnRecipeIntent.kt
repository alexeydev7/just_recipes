package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

sealed class MakeOwnRecipeIntent {
    data class GetNameRecipe(val name: String) : MakeOwnRecipeIntent()
    data class GetPortion(val portion: Int) : MakeOwnRecipeIntent()
    data class GetDuration(val duration: Int) : MakeOwnRecipeIntent()
    data object SaveOwnRecipe : MakeOwnRecipeIntent()
}