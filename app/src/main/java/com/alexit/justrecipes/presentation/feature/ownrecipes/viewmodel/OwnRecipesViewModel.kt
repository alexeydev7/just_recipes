package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.common.customDebounce
import com.alexit.justrecipes.common.customFlatMapLatest
import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel
import com.alexit.justrecipes.domain.usecase.GetOwnRecipesIdNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OwnRecipesViewModel @Inject constructor(
    private val getOwnRecipesIdNameUseCase: GetOwnRecipesIdNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnRecipesUiState())
    val uiState: StateFlow<OwnRecipesUiState> = _uiState.asStateFlow()
    val inputTextState = TextFieldState()


    val ownRecipesIdNameState: StateFlow<SourceState<List<RecipeIdNameModel>>> by lazy {
        snapshotFlow { inputTextState.text }
            .customDebounce(300)
            .distinctUntilChanged()
            .customFlatMapLatest { query ->
                getOwnRecipesIdNameUseCase(query.toString())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = SourceState.Loading
            )
    }

    fun handleIntent(intent: OwnRecipesIntent) {
        when (intent) {
            is OwnRecipesIntent.IsRemoveOwnRecipe -> isRemoveRecipe(intent.recipe)
            is OwnRecipesIntent.EditRecipe -> editRecipe(intent.recipe)
            is OwnRecipesIntent.ViewRecipe -> viewRecipe(intent.recipe)
        }
    }

    private fun isRemoveRecipe(recipe: RecipeIdNameModel) {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteRecipe = true,
                deletingRecipe = recipe
            )
        }
    }

    private fun editRecipe(recipe: RecipeIdNameModel) {
    }

    private fun viewRecipe(recipe: RecipeIdNameModel) {
        _uiState.update { currentState ->
            currentState.copy(
                showingRecipeId = recipe.id
            )
        }
    }
}