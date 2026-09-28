package com.alexit.justrecipes.presentation.feature.inputingrediets.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.common.StringResourceHolder
import com.alexit.justrecipes.domain.model.database.IngredientInputedModel
import com.alexit.justrecipes.domain.model.database.IngredientModelShort
import com.alexit.justrecipes.domain.usecase.AddInputtedIngredientUseCase
import com.alexit.justrecipes.domain.usecase.AddNewIngredientUseCase
import com.alexit.justrecipes.domain.usecase.ChangeWeightIngredientUseCase
import com.alexit.justrecipes.domain.usecase.GetCategoriesUseCase
import com.alexit.justrecipes.domain.usecase.GetIngredientUseCase
import com.alexit.justrecipes.domain.usecase.GetIngredientsNameUseCase
import com.alexit.justrecipes.domain.usecase.GetInputtedIngredientsUseCase
import com.alexit.justrecipes.domain.usecase.RemoveInputtedIngredientUseCase
import com.alexit.justrecipes.presentation.components.NotifySideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputIngredientsViewModel @Inject constructor(
    private val getIngredientUseCase: GetIngredientUseCase,
    private val getIngredientsNameUseCase: GetIngredientsNameUseCase,
    private val getInputtedIngredientsUseCase: GetInputtedIngredientsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addNewIngredientUseCase: AddNewIngredientUseCase,
    private val addInputtedIngredientUseCase: AddInputtedIngredientUseCase,
    private val removeInputtedIngredientUseCase: RemoveInputtedIngredientUseCase,
    private val changeWeightIngredientUseCase: ChangeWeightIngredientUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InputIngredientsUiState())
    val uiState: StateFlow<InputIngredientsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<NotifySideEffect>()
    val sideEffect: Flow<NotifySideEffect> = _sideEffect.receiveAsFlow()

    val inputtedIngredientsState: StateFlow<SourceState<List<IngredientInputedModel>>> by lazy {
        getInputtedIngredientsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SourceState.Loading
        )
    }

    val ingredientsNameState: StateFlow<SourceState<List<String>>> by lazy {
        getIngredientsNameUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SourceState.Loading
        )
    }

    val inputTextStateIngredient = TextFieldState()

    fun handleIntent(intent: InputIngredientsIntent) {
        when (intent) {
            is InputIngredientsIntent.SelectSuggestionIngredient -> selectSuggestionIngredient(intent.suggestion)
            is InputIngredientsIntent.CheckingSelectedIngredient -> checkingSelectedIngredient(intent.ingredientName)
            is InputIngredientsIntent.AddNewIngredient -> addNewIngredient(intent.ingredientCategory)
            is InputIngredientsIntent.DismissNewIngredient -> dismissNewIngredient()
            is InputIngredientsIntent.IsRemoveIngredient -> isRemoveIngredient(intent.ingredient)
            is InputIngredientsIntent.RemoveInputtedIngredient -> removeInputtedIngredient()
            is InputIngredientsIntent.DismissRemoveIngredient -> dismissRemoveIngredient()
            is InputIngredientsIntent.ChangeWeightIngredient -> changeWeightIngredient(
                intent.ingredientId, intent.ingredientWeight, intent.ingredientName
            )
        }
    }

    private fun checkingSelectedIngredient(ingredientName: String) {
        viewModelScope.launch {
            val addingIngredient: IngredientModelShort? = getIngredientUseCase(ingredientName)
            if (
                addingIngredient != null &&
                !addingIngredient.isInputted
            ) {
                try {
                    addInputtedIngredientUseCase(addingIngredient.id, addingIngredient.synonym)
                    inputTextStateIngredient.clearText()
                    _sideEffect.send(
                        NotifySideEffect.ShowNotify(
                            message = StringResourceHolder.StringResource(R.string.added_ingredient),
                            addition = addingIngredient.name,
                            state = NotifyState.INFO
                        )
                    )
                } catch (_: Exception) {
                    _sideEffect.send(
                        NotifySideEffect.ShowNotify(
                            message = StringResourceHolder.StringResource(R.string.hardware_error_add_ing),
                            addition = addingIngredient.name,
                            state = NotifyState.DANGER
                        )
                    )
                }
            } else if (
                addingIngredient != null
                ) {
                inputTextStateIngredient.clearText()
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.exist_ing),
                        addition = addingIngredient.name,
                        state = NotifyState.WARNING
                    )
                )
            } else {
                try {
                    val categories = getCategoriesUseCase()
                    _uiState.update { currentState ->
                        currentState.copy(
                            isIngredientNew = true,
                            newIngredientName = ingredientName,
                            categories = categories
                        )
                    }
                } catch (_: Exception) {
                    _sideEffect.send(
                        NotifySideEffect.ShowNotify(
                            message = StringResourceHolder.StringResource(R.string.hardware_error_get_cat),
                            state = NotifyState.DANGER
                        )
                    )
                }
            }
        }
    }

    private fun addNewIngredient(ingredientCategory: String) {
        viewModelScope.launch {
            try {
                addNewIngredientUseCase(
                    ingredientName = uiState.value.newIngredientName,
                    ingredientCategory = ingredientCategory
                )
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.added_new_ingredient),
                        addition = uiState.value.newIngredientName,
                        state = NotifyState.INFO
                    )
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        isIngredientNew = false,
                        newIngredientName = ""
                    )
                }
                inputTextStateIngredient.clearText()
            } catch (_: Exception) {
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.hardware_error_add_new_ing),
                        addition = uiState.value.newIngredientName,
                        state = NotifyState.DANGER
                    )
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        isIngredientNew = false,
                        newIngredientName = ""
                    )
                }
            }
        }
    }

    private fun selectSuggestionIngredient(suggestion: String) {
        inputTextStateIngredient.setTextAndPlaceCursorAtEnd(suggestion)
    }

    private fun dismissNewIngredient() {
        _uiState.update { currentState ->
            currentState.copy(
                isIngredientNew = false,
                newIngredientName = ""
            )
        }
    }

    private fun isRemoveIngredient(ingredient: IngredientInputedModel) {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteIngredient = true,
                deletingIngredientId = ingredient.id,
                deletingIngredientName = ingredient.name
            )
        }
    }

    fun dismissRemoveIngredient() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteIngredient = false,
                deletingIngredientId = -1,
                deletingIngredientName = ""
            )
        }
    }
    private fun removeInputtedIngredient() {
        viewModelScope.launch {
            try {
                removeInputtedIngredientUseCase(uiState.value.deletingIngredientId)
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.removed_ingredient),
                        addition = uiState.value.deletingIngredientName,
                        state = NotifyState.INFO
                    )
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        isDeleteIngredient = false,
                        deletingIngredientId = -1,
                        deletingIngredientName = ""
                    )
                }
            } catch (_: Exception) {
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.hardware_error_remove_ingredient),
                        addition = uiState.value.newIngredientName,
                        state = NotifyState.DANGER
                    )
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        isDeleteIngredient = false,
                        deletingIngredientId = -1,
                        deletingIngredientName = ""
                    )
                }
            }
        }
    }

    private fun changeWeightIngredient(ingredientId: Int, ingredientWeight: Int, ingredientName: String) {
        viewModelScope.launch {
            try {
                changeWeightIngredientUseCase(ingredientId, ingredientWeight)
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.change_weight_ingredient),
                        addition = ingredientName,
                        state = NotifyState.INFO
                    )
                )
            } catch (_: Exception) {
                _sideEffect.send(
                    NotifySideEffect.ShowNotify(
                        message = StringResourceHolder.StringResource(R.string.hardware_error_change_weight_ing),
                        state = NotifyState.DANGER
                    )
                )
            }
        }
    }
}
