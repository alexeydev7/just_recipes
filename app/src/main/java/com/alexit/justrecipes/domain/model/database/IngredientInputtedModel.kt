package com.alexit.justrecipes.domain.model.database

data class IngredientInputtedModel(
    val id: Int,
    val name: String,
    val category: String,
    val weight: Int? = null
)
