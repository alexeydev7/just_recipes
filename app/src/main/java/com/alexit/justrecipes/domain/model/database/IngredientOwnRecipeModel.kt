package com.alexit.justrecipes.domain.model.database

data class IngredientOwnRecipeModel(
    val id: Int,
    val name: String,
    val energy: Double,
    val protein: Double,
    val fat: Double,
    val carbohydrate: Double,
    val category: String,
    val weight: Int? = null
)
