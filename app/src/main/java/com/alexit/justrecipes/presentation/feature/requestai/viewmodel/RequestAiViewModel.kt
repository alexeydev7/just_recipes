package com.alexit.justrecipes.presentation.feature.requestai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.domain.model.database.IngredientInputtedModel
import com.alexit.justrecipes.domain.usecase.GetInputtedIngredientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RequestAiViewModel @Inject constructor(
    private val getInputtedIngredientsUseCase: GetInputtedIngredientsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestAiUiState())
    val uiState: StateFlow<RequestAiUiState> = _uiState.asStateFlow()

    val inputtedIngredientsState: StateFlow<SourceState<List<IngredientInputtedModel>>> by lazy {
        getInputtedIngredientsUseCase().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SourceState.Loading
        )
    }

    fun makeListIngredients(
        unitMl: String, unitPiece: String, unitG: String,
        liquidFood: Array<String>, pieceFood: Array<String>,
        inputtedIngredients: List<IngredientInputtedModel>
    ) {
        var listIngredients = ""
        inputtedIngredients.forEach { ingredient ->
            val amountIngredient = if (ingredient.weight != null) {
                val unit: String = if (liquidFood.contains(ingredient.category)) unitMl
                else if (pieceFood.contains(ingredient.category)) unitPiece
                else unitG
                " ${ingredient.weight} $unit"
            } else ""
            listIngredients += "${ingredient.name}${amountIngredient}, "
        }
        _uiState.update { currentState ->
            currentState.copy(
                listIngredients = listIngredients
            )
        }
    }

    fun selectDishType(isFirst: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isDishFirst = isFirst
            )
        }
    }

    fun changeRequestTask() {
        _uiState.update { currentState ->
            val isRequestOk = uiState.value.isRequestOk
            currentState.copy(isRequestOk = !isRequestOk)
        }
    }
}