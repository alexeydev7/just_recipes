package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.common.StringResourceHolder
import com.alexit.justrecipes.common.customDebounce
import com.alexit.justrecipes.common.customFlatMapLatest
import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel
import com.alexit.justrecipes.domain.usecase.DeleteOwnRecipeUseCase
import com.alexit.justrecipes.domain.usecase.GetOwnRecipesIdNameUseCase
import com.alexit.justrecipes.presentation.components.NotifySideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnRecipesViewModel @Inject constructor(
    private val getOwnRecipesIdNameUseCase: GetOwnRecipesIdNameUseCase,
    private val deleteOwnRecipeUseCase: DeleteOwnRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnRecipesUiState())
    val uiState: StateFlow<OwnRecipesUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<NotifySideEffect>()
    val sideEffect: Flow<NotifySideEffect> = _sideEffect.receiveAsFlow()

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
            is OwnRecipesIntent.IsDeleteOwnRecipe -> isDeleteRecipe(recipe = intent.recipe)
            is OwnRecipesIntent.DeleteOwnRecipe -> deleteRecipe()
            is OwnRecipesIntent.DismissDeleteOwnRecipe -> dismissDeleteRecipe()
            is OwnRecipesIntent.EditRecipe -> editRecipe(recipe = intent.recipe)
            is OwnRecipesIntent.ViewRecipe -> viewRecipe(recipe = intent.recipe)
        }
    }

    private fun isDeleteRecipe(recipe: RecipeIdNameModel) {
        _uiState.update { currentState ->
            currentState.copy(
                deletingRecipe = recipe
            )
        }
    }

    private fun deleteRecipe() {
        viewModelScope.launch {
            try {
                deleteOwnRecipeUseCase(uiState.value.deletingRecipe.id)
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.deleted_recipe),
                        addition = uiState.value.deletingRecipe.name,
                        state = NotifyState.INFO
                    )
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        deletingRecipe = RecipeIdNameModel(-1, "")
                    )
                }
            } catch (_: Exception) {
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.hardware_error_delete_recipe),
                        addition = uiState.value.deletingRecipe.name,
                        state = NotifyState.DANGER
                    )
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        deletingRecipe = RecipeIdNameModel(-1, "")
                    )
                }
            }
        }
    }

    private fun dismissDeleteRecipe() {
        _uiState.update { currentState ->
            currentState.copy(
                deletingRecipe = RecipeIdNameModel(-1, "")
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