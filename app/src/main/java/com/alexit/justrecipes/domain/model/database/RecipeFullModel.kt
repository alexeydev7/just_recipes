package com.alexit.justrecipes.domain.model.database

data class RecipeFullModel(
    val id: Int,
    val name: String,
    val image: String,
    val portion: Int?,
    val duration: Int?,
    val energy: Double,
    val protein: Double,
    val fat: Double,
    val carbohydrate: Double,
    val details: String?,
    val detailsImage: String?,
    val ingredients: List<IngredientDataModel>
)
