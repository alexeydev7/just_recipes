package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexit.justrecipes.common.NotifySideEffect
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.domain.model.database.IngredientOwnRecipeModel
import com.alexit.justrecipes.domain.usecase.GetIngredientsOwnRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MakeOwnRecipeViewModel @Inject constructor(
    private val getIngredientsOwnRecipeUseCase: GetIngredientsOwnRecipeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MakeOwnRecipeUiState())
    val uiState: StateFlow<MakeOwnRecipeUiState> = _uiState.asStateFlow()

    val ingredientsOwnRecipeState: StateFlow<SourceState<List<IngredientOwnRecipeModel>>> by lazy {
        getIngredientsOwnRecipeUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SourceState.Loading
        )
    }

    private val _sideEffect = Channel<NotifySideEffect>()
    val sideEffect: Flow<NotifySideEffect> = _sideEffect.receiveAsFlow()

    val inputNameRecipeState = TextFieldState()
    val inputPortionState = TextFieldState()
    val inputDurationState = TextFieldState()

    fun handleIntent(intent: MakeOwnRecipeIntent) {
        when (intent) {
            is MakeOwnRecipeIntent.GetNameRecipe -> getNameRecipe(intent.name)
            is MakeOwnRecipeIntent.SaveOwnRecipe -> saveOwnRecipe()
            is MakeOwnRecipeIntent.GetPortion -> getPortion(intent.portion)
            is MakeOwnRecipeIntent.GetDuration -> getDuration(intent.duration)
        }
    }

    private fun getNameRecipe(name: String) {
        _uiState.value = _uiState.value.copy(
            nameRecipe = name.replaceFirstChar { it.titlecase() }
        )
        inputNameRecipeState.clearText()
    }

    private fun getPortion(portion: Int) {
        _uiState.value = _uiState.value.copy(
            portion = portion
        )
    }

    private fun getDuration(duration: Int) {
        _uiState.value = _uiState.value.copy(
            duration = duration
        )
    }

    private fun saveOwnRecipe() {}
}