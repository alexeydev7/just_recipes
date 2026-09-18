package com.alexit.justrecipes.presentation.feature.searchrecipes.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alexit.justrecipes.common.customDebounce
import com.alexit.justrecipes.common.customFlatMapLatest
import com.alexit.justrecipes.domain.model.database.RecipeCardModel
import com.alexit.justrecipes.domain.usecase.GetRecipeCardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class SearchRecipesViewModel @Inject constructor(
    private val getRecipeCardDataUseCase: GetRecipeCardDataUseCase,
) : ViewModel() {
    val inputTextState = TextFieldState()

    val recipeCardData: Flow<PagingData<RecipeCardModel>> =
        snapshotFlow { inputTextState.text }
            .customDebounce(300)
            .distinctUntilChanged()
            .customFlatMapLatest { query ->
                getRecipeCardDataUseCase(query.toString())
            }
            .cachedIn(viewModelScope)

}
