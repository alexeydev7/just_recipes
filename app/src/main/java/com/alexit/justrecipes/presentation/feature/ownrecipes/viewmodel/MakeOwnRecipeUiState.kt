package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

data class MakeOwnRecipeUiState(
    val isSaved: Boolean = false,
    val nameRecipe: String = "",
    val portion: Int = 0,
    val duration: Int = 0
)