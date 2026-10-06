package com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MakeOwnRecipeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MakeOwnRecipeUiState())
    val uiState: StateFlow<MakeOwnRecipeUiState> = _uiState.asStateFlow()

    val inputNameRecipeState = TextFieldState()

    fun handleIntent(intent: MakeOwnRecipeIntent) {
        when (intent) {
            is MakeOwnRecipeIntent.GetNameRecipe -> getNameRecipe(intent.name)
            is MakeOwnRecipeIntent.SaveOwnRecipe -> saveOwnRecipe()
        }
    }

    private fun getNameRecipe(name: String) {
        _uiState.value = _uiState.value.copy(
            nameRecipe = name.replaceFirstChar { it.titlecase() }
        )
        //inputNameRecipeState.clearText()
    }
    private fun saveOwnRecipe() {}
}