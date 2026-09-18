package com.alexit.justrecipes.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

@Dao
interface RecipesDao {
    @Query("SELECT id, name, synonym, is_inputted AS isInputted " +
            "FROM ingredients WHERE name = :ingredientName")
    suspend fun getIngredient(ingredientName: String): IngredientModelShort?

    @Query("SELECT name FROM ingredients")
    fun getIngredientsName(): Flow<List<String>>

    @Query("SELECT id, name, category, weight FROM ingredients WHERE is_inputted = 1 " +
            "ORDER BY name")
    fun getInputtedIngredients(): Flow<List<IngredientInputedModel>>

    @Query("SELECT DISTINCT category FROM ingredients ORDER BY category")
    suspend fun getCategories(): List<String>

    //@Query("SELECT EXISTS(SELECT 1 FROM ingredients WHERE name = :ingredientName)")
    //suspend fun checkExistIngredient(ingredientName: String): Boolean

    @Query("SELECT id, name FROM ingredients")
    suspend fun getIngredientIdName(): List<IngredientIdNameModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnIngredient(ingredient: IngredientEntity)

    @Query("UPDATE ingredients " +
            "SET " +
            "is_inputted = CASE WHEN id = :ingredientId THEN 1 " +
            "ELSE is_inputted " +
            "END, " +
            "is_synonym = CASE WHEN synonym = :synonym THEN is_synonym + 1 " +
            "ELSE is_synonym " +
            "END " +
            "WHERE (id = :ingredientId OR synonym = :synonym)")
    suspend fun insertInputtedIngredient(ingredientId: Int, synonym: String)

    @Query("UPDATE ingredients " +
            "SET " +
            "weight = CASE WHEN id = :ingredientId " +
            "THEN NULL " +
            "ELSE weight " +
            "END, " +
            "is_inputted = CASE WHEN id = :ingredientId " +
            "THEN 0 " +
            "ELSE is_inputted " +
            "END, " +
            "is_synonym = CASE WHEN synonym = (SELECT synonym FROM ingredients WHERE id = :ingredientId) " +
            "THEN is_synonym - 1 " +
            "ELSE is_synonym " +
            "END " +
            "WHERE (" +
            "id = :ingredientId OR synonym = (SELECT synonym FROM ingredients WHERE id = :ingredientId))")
    suspend fun deleteInputtedIngredient(ingredientId: Int)

    @Query("UPDATE ingredients SET weight = :ingredientWeight WHERE id = :ingredientId")
    suspend fun updateWeightIngredient(ingredientId: Int, ingredientWeight: Int)

    @Query("SELECT AVG(DISTINCT energy) FROM ingredients WHERE category = :category")
    suspend fun getAVGEnergy(category: String): Double

    @Query("SELECT AVG(DISTINCT protein)FROM ingredients WHERE category = :category")
    suspend fun getAVGProtein(category: String): Double

    @Query("SELECT AVG(DISTINCT fat)FROM ingredients WHERE category = :category")
    suspend fun getAVGFat(category: String): Double

    @Query("SELECT AVG(DISTINCT carbohydrate)FROM ingredients WHERE category = :category")
    suspend fun getAVGCarbohydrate(category: String): Double

    @Query("SELECT MAX(id) FROM ingredients")
    suspend fun getMAXIdIngredients(): Int

    @Query("SELECT MAX(id) FROM recipes")
    suspend fun getMAXIdRecipes(): Int

    @Query("SELECT " +
            "recipes.id, recipes.name, recipes.duration, recipes.portion, recipes.image, " +
            "SUM(ingredients.is_synonym) AS ingredientsOk, " +
            "SUM(NOT ingredients.is_synonym) AS ingredientsNo, " +
            "(0) AS isHealthy " +
            "FROM recipe_ingredients " +
            "INNER JOIN recipes ON recipes.id = recipe_ingredients.recipe_id " +
            "INNER JOIN ingredients ON ingredients.id = recipe_ingredients.ingredient_id " +
            "WHERE recipes.name LIKE :query " +
            "GROUP BY recipes.id " +
            "ORDER BY ingredientsNo ASC, ingredientsOk DESC, recipes.duration")
    fun getRecipesCardData(query: String): PagingSource<Int, RecipeCardModel>

    @Query("SELECT " +
            "ingredients.id, ingredients.energy, ingredients.protein, ingredients.fat, " +
            "ingredients.carbohydrate, " +
            "recipe_ingredients.quantity, recipe_ingredients.density " +
            "FROM recipe_ingredients " +
            "INNER JOIN recipes ON recipes.id = recipe_ingredients.recipe_id " +
            "INNER JOIN ingredients ON ingredients.id = recipe_ingredients.ingredient_id " +
            "WHERE recipe_ingredients.recipe_id = :recipeId")
    suspend fun getIngredientsEnergy(recipeId: Int): List<IngredientModelEnergy>

    @Query("SELECT " +
            "id, name, image, duration, portion, details, details_img AS detailsImage " +
            "FROM recipes " +
            "WHERE id = :recipeId")
    suspend fun getRecipeData(recipeId: Int): RecipeDataModel

    @Query("SELECT " +
            "ingredients.id, ingredients.name, ingredients.energy, " +
            "ingredients.protein, ingredients.fat, ingredients.carbohydrate, ingredients.weight, " +
            "ingredients.is_synonym AS isSynonym, " +
            "recipe_ingredients.quantity, recipe_ingredients.unit, recipe_ingredients.density " +
            "FROM recipe_ingredients " +
            "INNER JOIN recipes ON recipes.id = recipe_ingredients.recipe_id " +
            "INNER JOIN ingredients ON ingredients.id = recipe_ingredients.ingredient_id " +
            "WHERE recipe_ingredients.recipe_id = :recipeId")
    suspend fun getIngredientsData(recipeId: Int): List<IngredientModelFull>

    @Query("SELECT " +
            "id, name " +
            "FROM recipes " +
            "WHERE (image = 'ai' OR image == 'own') " +
            "AND name LIKE :query " +
            "ORDER BY name")
    fun getOwnRecipesIdName(query: String): Flow<List<RecipeIdNameModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredients(recipeIngredients: List<RecipeIngredientsEntity>)

    @Transaction
    suspend fun addNewRecipe(
        recipe: RecipeEntity,
        recipeIngredients: List<RecipeIngredientsEntity>
    ) {
        insertOwnRecipe(recipe)
        insertRecipeIngredients(recipeIngredients)
    }
}