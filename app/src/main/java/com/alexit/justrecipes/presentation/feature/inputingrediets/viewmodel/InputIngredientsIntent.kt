package com.alexit.justrecipes.presentation.feature.inputingrediets.viewmodel

import com.alexit.justrecipes.domain.model.database.IngredientInputtedModel

sealed class InputIngredientsIntent {
    data class SelectSuggestionIngredient(val suggestion: String) : InputIngredientsIntent()
    data class CheckingSelectedIngredient(val ingredientName: String) : InputIngredientsIntent()
    data class AddNewIngredient(val ingredientCategory: String) : InputIngredientsIntent()
    data object DismissNewIngredient: InputIngredientsIntent()
    data class IsRemoveIngredient(val ingredient: IngredientInputtedModel) : InputIngredientsIntent()
    data object RemoveInputtedIngredient : InputIngredientsIntent()
    data object DismissRemoveIngredient : InputIngredientsIntent()
    data class ChangeWeightIngredient(
        val ingredientId: Int,
        val ingredientWeight: Int,
        val ingredientName: String
    ) : InputIngredientsIntent()
}
