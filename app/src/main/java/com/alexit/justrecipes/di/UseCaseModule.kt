package com.alexit.justrecipes.di

import com.alexit.justrecipes.domain.remote.KtorApiService
import com.alexit.justrecipes.domain.usecase.AddNewIngredientUseCase
import com.alexit.justrecipes.domain.repository.RecipesRepository
import com.alexit.justrecipes.domain.usecase.AddInputtedIngredientUseCase
import com.alexit.justrecipes.domain.usecase.AddAiRecipeUseCase
import com.alexit.justrecipes.domain.usecase.ChangeWeightIngredientUseCase
import com.alexit.justrecipes.domain.usecase.GetCategoriesUseCase
import com.alexit.justrecipes.domain.usecase.GetIngredientUseCase
import com.alexit.justrecipes.domain.usecase.GetIngredientsNameUseCase
import com.alexit.justrecipes.domain.usecase.GetInputtedIngredientsUseCase
import com.alexit.justrecipes.domain.usecase.GetMAXIdIngredientsUseCase
import com.alexit.justrecipes.domain.usecase.GetOwnRecipesIdNameUseCase
import com.alexit.justrecipes.domain.usecase.GetRecipeAiUseCase
import com.alexit.justrecipes.domain.usecase.GetRecipeCardDataUseCase
import com.alexit.justrecipes.domain.usecase.GetRecipeFullDataUseCase
import com.alexit.justrecipes.domain.usecase.RemoveInputtedIngredientUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetIngredientUseCase(recipesRepository: RecipesRepository): GetIngredientUseCase {
        return GetIngredientUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetIngredientsNameUseCase(recipesRepository: RecipesRepository): GetIngredientsNameUseCase {
        return GetIngredientsNameUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetInputtedIngredientsUseCase(recipesRepository: RecipesRepository): GetInputtedIngredientsUseCase {
        return GetInputtedIngredientsUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(recipesRepository: RecipesRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideAddNewIngredientUseCase(recipesRepository: RecipesRepository): AddNewIngredientUseCase {
        return AddNewIngredientUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideAddInputtedIngredientUseCase(recipesRepository: RecipesRepository): AddInputtedIngredientUseCase {
        return AddInputtedIngredientUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveInputtedIngredientUseCase(recipesRepository: RecipesRepository): RemoveInputtedIngredientUseCase {
        return RemoveInputtedIngredientUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideChangeWeightIngredientUseCase(recipesRepository: RecipesRepository): ChangeWeightIngredientUseCase {
        return ChangeWeightIngredientUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetMAXIdIngredientsUseCase(recipesRepository: RecipesRepository): GetMAXIdIngredientsUseCase {
        return GetMAXIdIngredientsUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeCardDataUseCase(recipesRepository: RecipesRepository): GetRecipeCardDataUseCase {
        return GetRecipeCardDataUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeFullDataUseCase(recipesRepository: RecipesRepository): GetRecipeFullDataUseCase {
        return GetRecipeFullDataUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeAiUseCase(ktorApiService: KtorApiService): GetRecipeAiUseCase {
        return GetRecipeAiUseCase(ktorApiService)
    }

    @Provides
    @Singleton
    fun provideAddAiRecipeUseCase(recipesRepository: RecipesRepository): AddAiRecipeUseCase {
        return AddAiRecipeUseCase(recipesRepository)
    }

    @Provides
    @Singleton
    fun provideGetOwnRecipesIdNameUseCase(recipesRepository: RecipesRepository): GetOwnRecipesIdNameUseCase {
        return GetOwnRecipesIdNameUseCase(recipesRepository)
    }
}