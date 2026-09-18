package com.alexit.justrecipes.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.common.asSourceState
import com.alexit.justrecipes.data.local.room.dao.RecipesDao
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
import com.alexit.justrecipes.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val PAGE_SIZE = 30

class RecipesRepositoryImpl @Inject constructor(
    private val recipesDao: RecipesDao
) : RecipesRepository {

    override suspend fun getIngredient(ingredientName: String): IngredientModelShort? {
        return recipesDao.getIngredient(ingredientName)
    }

    override fun getIngredientsName(): Flow<SourceState<List<String>>> {
        return recipesDao.getIngredientsName().asSourceState()
    }

    override fun getInputtedIngredients(): Flow<SourceState<List<IngredientInputedModel>>> {
        return recipesDao.getInputtedIngredients().asSourceState()
    }

    override suspend fun getCategories(): List<String> {
        return recipesDao.getCategories()
    }

    //override suspend fun checkExistIngredient(ingredientName: String): Boolean {
    //    return recipesDao.checkExistIngredient(ingredientName)
    //}

    override suspend fun getIngredientIdName(): List<IngredientIdNameModel> {
        return recipesDao.getIngredientIdName()
    }

    override suspend fun addNewIngredient(ingredient: IngredientEntity) {
        recipesDao.insertOwnIngredient(ingredient)
    }

    override suspend fun addInputtedIngredient(ingredientId: Int, synonym: String) {
        recipesDao.insertInputtedIngredient(ingredientId, synonym)
    }

    override suspend fun removeInputtedIngredient(ingredientId: Int) {
        recipesDao.deleteInputtedIngredient(ingredientId)
    }

    override suspend fun changeWeightIngredient(ingredientId: Int, ingredientWeight: Int) {
        recipesDao.updateWeightIngredient(ingredientId, ingredientWeight)
    }

    override suspend fun getAVGEnergy(category: String): Double {
        return recipesDao.getAVGEnergy(category)
    }

    override suspend fun getAVGProtein(category: String): Double {
        return recipesDao.getAVGProtein(category)
    }

    override suspend fun getAVGFat(category: String): Double {
        return recipesDao.getAVGFat(category)
    }

    override suspend fun getAVGCarbohydrate(category: String): Double {
        return recipesDao.getAVGCarbohydrate(category)
    }

    override suspend fun getMAXIdIngredients(): Int {
        return recipesDao.getMAXIdIngredients()
    }

    override suspend fun getMAXIdRecipes(): Int {
        return recipesDao.getMAXIdRecipes()
    }

    override fun getRecipesCardData(query: String): Flow<PagingData<RecipeCardModel>> {
        val formattedQuery = "%$query%"
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                recipesDao.getRecipesCardData(formattedQuery)
            }
        ).flow
    }

    override suspend fun getIngredientsEnergy(recipeId: Int): List<IngredientModelEnergy> {
        return recipesDao.getIngredientsEnergy(recipeId)
    }

    override suspend fun getRecipeData(recipeId: Int): RecipeDataModel {
        return recipesDao.getRecipeData(recipeId)
    }

    override suspend fun getIngredientsData(recipeId: Int): List<IngredientModelFull> {
        return recipesDao.getIngredientsData(recipeId)
    }

    override fun getOwnRecipesIdName(query: String): Flow<SourceState<List<RecipeIdNameModel>>> {
        val formattedQuery = "%$query%"
        return recipesDao.getOwnRecipesIdName(formattedQuery).asSourceState()
    }

    override suspend fun addNewRecipe(
        recipe: RecipeEntity,
        recipeIngredients: List<RecipeIngredientsEntity>
    ) {
        recipesDao.addNewRecipe(recipe, recipeIngredients)
    }
}
