package com.alexit.justrecipes.presentation.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.alexit.justrecipes.presentation.navigation.AnswerAi
import com.alexit.justrecipes.presentation.navigation.BOTTOM_MENU_ROUTES
import com.alexit.justrecipes.presentation.navigation.InputIngredientsTab
import com.alexit.justrecipes.presentation.navigation.MakeOwnRecipe
import com.alexit.justrecipes.presentation.navigation.RecipesBottomMenu
import com.alexit.justrecipes.presentation.navigation.RecipesNavigationState
import com.alexit.justrecipes.presentation.navigation.RecipesNavigator
import com.alexit.justrecipes.presentation.navigation.ShowOwnRecipe
import com.alexit.justrecipes.presentation.navigation.ShowRecipe
import com.alexit.justrecipes.presentation.navigation.featureInputIngredients
import com.alexit.justrecipes.presentation.navigation.featureOwnRecipes
import com.alexit.justrecipes.presentation.navigation.featureRequestAi
import com.alexit.justrecipes.presentation.navigation.featureSearchRecipes
import com.alexit.justrecipes.presentation.navigation.rememberRecipesNavigationState
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun RecipesScreen(
) {
    val navigationState: RecipesNavigationState = rememberRecipesNavigationState(
        startTab = InputIngredientsTab,
        tabs = BOTTOM_MENU_ROUTES.keys
    )
    val navigator: RecipesNavigator = remember { RecipesNavigator(navigationState) }
    val entryProvider = entryProvider {
        featureInputIngredients()
        featureSearchRecipes(
            onSubRouteClick = { id -> navigator.navigateTo(ShowRecipe(recipeId = id)) },
            onBackClick = { navigator.navigationBack() })
        featureRequestAi(
            onSubRouteClick = { prompt -> navigator.navigateTo(AnswerAi(prompt = prompt)) },
            onBackClick = { navigator.navigationBack() }
        )
        featureOwnRecipes(
            onSubRouteClick1 = { navigator.navigateTo(MakeOwnRecipe) },
            onSubRouteClick2 = { id -> navigator.navigateTo(ShowOwnRecipe(recipeId = id)) },
            onBackClick = { navigator.navigationBack() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            NavDisplay(
                entries = navigationState.toDecoratedEntries(entryProvider),
                onBack = { navigator.navigationBack() }
            )
        }
        Row(
            modifier = Modifier
                .height(JustRecipesTheme.dimensions.heightBottomMenu)
                .background(JustRecipesTheme.colors.bottomMenu)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RecipesBottomMenu(
                navigationState = navigationState,
                navigator = navigator
            )
        }
    }
}
