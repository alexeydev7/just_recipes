package com.alexit.justrecipes.domain.model.database

data class IngredientShortModel(
    val id: Int,
    val name: String,
    val synonym: String?,
    val isInputted: Boolean
)
