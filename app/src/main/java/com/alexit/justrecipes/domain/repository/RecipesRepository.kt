package com.alexit.justrecipes.domain.repository

import androidx.paging.PagingData
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.data.local.room.entity.IngredientEntity
import com.alexit.justrecipes.data.local.room.entity.RecipeEntity
import com.alexit.justrecipes.data.local.room.entity.RecipeIngredientsEntity
import com.alexit.justrecipes.domain.model.database.IngredientIdNameModel
import com.alexit.justrecipes.domain.model.database.IngredientInputedModel
import com.alexit.justrecipes.domain.model.database.IngredientModelEnergy
import com.alexit.justrecipes.domain.model.database.IngredientModelFull
import com.alexit.justrecipes.domain.model.database.IngredientModelShort
import com.alexit.justrecipes.domain.model.database.RecipeCardModel
import com.alexit.justrecipes.domain.model.database.RecipeDataModel
import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    suspend fun getIngredient(ingredientName: String): IngredientModelShort?
    fun getIngredientsName(): Flow<SourceState<List<String>>>
    fun getInputtedIngredients(): Flow<SourceState<List<IngredientInputedModel>>>
    suspend fun getCategories(): List<String>
    //suspend fun checkExistIngredient(ingredientName: String): Boolean
    suspend fun getIngredientIdName(): List<IngredientIdNameModel>
    suspend fun addNewIngredient(ingredient: IngredientEntity)
    suspend fun addInputtedIngredient(ingredientId: Int, synonym: String)
    suspend fun removeInputtedIngredient(ingredientId: Int)
    suspend fun changeWeightIngredient(ingredientId: Int, ingredientWeight: Int)
    suspend fun getAVGEnergy(category: String): Double
    suspend fun getAVGProtein(category: String): Double
    suspend fun getAVGFat(category: String): Double
    suspend fun getAVGCarbohydrate(category: String): Double
    suspend fun getMAXIdIngredients(): Int
    suspend fun getMAXIdRecipes(): Int
    fun getRecipesCardData(query: String): Flow<PagingData<RecipeCardModel>>
    suspend fun getIngredientsEnergy(recipeId: Int): List<IngredientModelEnergy>
    suspend fun getRecipeData(recipeId: Int): RecipeDataModel
    suspend fun getIngredientsData(recipeId: Int): List<IngredientModelFull>
    fun getOwnRecipesIdName(query: String): Flow<SourceState<List<RecipeIdNameModel>>>
    suspend fun addNewRecipe(recipe: RecipeEntity, recipeIngredients: List<RecipeIngredientsEntity>)
}