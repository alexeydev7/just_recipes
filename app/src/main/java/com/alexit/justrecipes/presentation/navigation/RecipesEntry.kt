package com.alexit.justrecipes.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.alexit.justrecipes.presentation.feature.inputingrediets.InputIngredientsScreen
import com.alexit.justrecipes.presentation.feature.ownrecipes.MakeOwnRecipeScreen
import com.alexit.justrecipes.presentation.feature.ownrecipes.OwnRecipesScreen
import com.alexit.justrecipes.presentation.feature.requestai.AnswerAiScreen
import com.alexit.justrecipes.presentation.feature.requestai.RequestAiScreen
import com.alexit.justrecipes.presentation.feature.searchrecipes.SearchRecipesScreen
import com.alexit.justrecipes.presentation.feature.searchrecipes.ShowRecipeScreen

fun EntryProviderScope<NavKey>.featureInputIngredients(
) {
    entry<InputIngredientsTab> {
        InputIngredientsScreen()
    }
}

fun EntryProviderScope<NavKey>.featureSearchRecipes(
    onSubRouteClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    entry<SearchRecipesTab> {
        SearchRecipesScreen(
            onRecipeClick = { id: Int -> onSubRouteClick(id) }
        )
    }

    entry<ShowRecipe> { key ->
        ShowRecipeScreen(
            recipeId = key.recipeId,
            onBackClick = onBackClick
        )
    }
}

fun EntryProviderScope<NavKey>.featureRequestAi(
    onSubRouteClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    entry<RequestAiTab> {
        RequestAiScreen(
            onPromptClick = { prompt: String -> onSubRouteClick(prompt) }
        )
    }

    entry<AnswerAi> { key ->
        AnswerAiScreen(
            promptUser = key.prompt,
            onBackClick = onBackClick
        )
    }
}

fun EntryProviderScope<NavKey>.featureOwnRecipes(
    onSubRouteClick1: () -> Unit,
    onSubRouteClick2: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    entry<OwnRecipesTab> {
        OwnRecipesScreen(
            onAddRecipeClick = onSubRouteClick1,
            onRecipeClick = { id: Int -> onSubRouteClick2(id) }
        )
    }

    entry< ShowOwnRecipe> { key ->
        ShowRecipeScreen(
            recipeId = key.recipeId,
            onBackClick = onBackClick
        )
    }

    entry<MakeOwnRecipe> {
        MakeOwnRecipeScreen(
            onBackClick = onBackClick
        )
    }
}