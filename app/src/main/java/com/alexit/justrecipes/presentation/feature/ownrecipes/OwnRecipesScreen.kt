package com.alexit.justrecipes.presentation.feature.ownrecipes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel
import com.alexit.justrecipes.presentation.components.CircleLoader
import com.alexit.justrecipes.presentation.components.CustomPopup
import com.alexit.justrecipes.presentation.components.CustomTextField
import com.alexit.justrecipes.presentation.components.TitlePanel
import com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel.OwnRecipesViewModel
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun OwnRecipesScreen(
    ownRecipesViewModel: OwnRecipesViewModel = hiltViewModel(),
    onAddRecipeClick: () -> Unit
) {
    val ownRecipesUiState by ownRecipesViewModel.uiState.collectAsStateWithLifecycle()
    val ownRecipesIdNameState = ownRecipesViewModel.ownRecipesIdNameState.collectAsStateWithLifecycle()

    var isNewNotify by remember { mutableStateOf(false) }
    var notifyMessage by remember { mutableStateOf("") }
    var notifyState by remember { mutableStateOf(NotifyState.INFO) }

    if (isNewNotify) {
        CustomPopup(
            message = notifyMessage,
            state = notifyState,
            onDismissRequest = { isNewNotify = !isNewNotify }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TitlePanel(
            text = stringResource(R.string.title_own_recipes),
            onRightClick = onAddRecipeClick,
            textRight = stringResource(R.string.add_recipe)
        )
        CustomTextField(
            state = ownRecipesViewModel.inputTextState,
            iconPlaceholder = R.drawable.text_search,
            iconDescriptionPlaceholder = R.string.icon_text_search,
            placeholder = stringResource(R.string.placeholder_search_recipes)
        )

        if (ownRecipesUiState.recipeId == 0){
            when(val sourceState = ownRecipesIdNameState.value) {
                is SourceState.Loading -> LoadingScreen()
                is SourceState.Success -> ShowOwnRecipes(
                    listOwnRecipes =  sourceState.data.toPersistentList()
                )
                is SourceState.Error -> {
                    isNewNotify = true
                    notifyMessage = if (sourceState.message != null) {
                        "${stringResource(R.string.hardware_error)}\n${sourceState.message}"
                    } else {
                        stringResource(R.string.unknown_error_occurred)
                    }
                    notifyState = NotifyState.DANGER
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    var isLoading by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircleLoader(
            color = JustRecipesTheme.colors.circleLoader,
            modifier = Modifier.size(JustRecipesTheme.dimensions.sizeCircleLoader),
            isVisible = isLoading
        )
    }
}

@Composable
private fun ShowOwnRecipes(listOwnRecipes: PersistentList<RecipeIdNameModel>) {
    LazyColumn {
        items(items = listOwnRecipes, key = { it.id }) { recipe ->
            BasicText(
                text = recipe.name
            )
        }
    }
}
