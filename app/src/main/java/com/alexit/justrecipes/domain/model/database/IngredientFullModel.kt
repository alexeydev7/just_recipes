package com.alexit.justrecipes.domain.model.database

data class IngredientFullModel(
    val id: Int,
    val name: String,
    val energy: Double,
    val protein: Double,
    val fat: Double,
    val carbohydrate: Double,
    val quantity: Double,
    val unit: String?,
    val density: Double,
    val weight: Int?,
    val isSynonym: Int
)
