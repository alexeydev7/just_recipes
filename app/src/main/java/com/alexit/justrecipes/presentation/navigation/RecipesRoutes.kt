package com.alexit.justrecipes.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.alexit.justrecipes.R
import kotlinx.serialization.Serializable


@Serializable
data object InputIngredientsTab : NavKey

@Serializable
data object SearchRecipesTab : NavKey

@Serializable
data class ShowRecipe(val recipeId: Int) : NavKey

@Serializable
data class ShowOwnRecipe(val recipeId: Int) : NavKey

@Serializable
data object RequestAiTab : NavKey

@Serializable
data class AnswerAi(val prompt: String) : NavKey

@Serializable
data object OwnRecipesTab : NavKey

@Serializable
data object MakeOwnRecipe : NavKey

val BOTTOM_MENU_ROUTES = mapOf<NavKey, BottomMenuItem>(
    InputIngredientsTab to BottomMenuItem(
        icon = R.drawable.format_list_bulleted_24px,
        iconDescription = R.string.button_input_ingredients
    ),
    SearchRecipesTab to BottomMenuItem(
        icon = R.drawable.menu_book_24px,
        iconDescription = R.string.button_recipes
    ),
    RequestAiTab to BottomMenuItem(
        icon = R.drawable.psychology_24px,
        iconDescription = R.string.button_request_ai
    ),
    OwnRecipesTab to BottomMenuItem(
        icon = R.drawable.note_stack_24px,
        iconDescription = R.string.button_own_recipes
    )
)

data class BottomMenuItem(val icon: Int, val iconDescription: Int )
