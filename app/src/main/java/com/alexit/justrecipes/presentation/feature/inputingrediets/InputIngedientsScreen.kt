package com.alexit.justrecipes.presentation.feature.inputingrediets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.domain.model.database.IngredientInputedModel
import com.alexit.justrecipes.presentation.components.CircleLoader
import com.alexit.justrecipes.presentation.components.CustomDialog
import com.alexit.justrecipes.presentation.components.CustomPopup
import com.alexit.justrecipes.presentation.components.CustomTextField
import com.alexit.justrecipes.presentation.components.TitlePanel
import com.alexit.justrecipes.presentation.feature.inputingrediets.viewmodel.InputIngredientsIntent
import com.alexit.justrecipes.presentation.feature.inputingrediets.viewmodel.InputIngredientsViewModel
import com.alexit.justrecipes.presentation.components.NotifySideEffect
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InputIngredientsScreen(
   inputIngredientsViewModel: InputIngredientsViewModel = hiltViewModel()
) {
    val inputIngredientsUiState by inputIngredientsViewModel.uiState.collectAsStateWithLifecycle()
    val ingredientsNameState = inputIngredientsViewModel.ingredientsNameState.collectAsStateWithLifecycle()
    val inputtedIngredientsState = inputIngredientsViewModel.inputtedIngredientsState.collectAsStateWithLifecycle()

    val paddingFieldInput = JustRecipesTheme.dimensions.paddingFieldInput

    var isNewNotify by remember { mutableStateOf(false) }
    var notifyMessage by remember { mutableStateOf("") }
    var notifyState by remember { mutableStateOf(NotifyState.INFO) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        inputIngredientsViewModel.sideEffect.collectLatest { notify ->
            when (notify) {
                is NotifySideEffect.ShowNotify -> {
                    isNewNotify = true
                    notifyMessage = "${notify.message.asString(context)}\n${notify.addition}".trimEnd()
                    notifyState = notify.state
                }
            }
        }
    }

    if (isNewNotify) {
        CustomPopup(
            message = notifyMessage,
            state = notifyState,
            onDismissRequest = { isNewNotify = !isNewNotify }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
        ) {
        TitlePanel(
            text = stringResource(R.string.title_input_ingredients),
        )

        Column(
            modifier = Modifier
                .padding(vertical = paddingFieldInput)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                state = inputIngredientsViewModel.inputTextStateIngredient,
                onDoneClick = { ingredientName: String ->
                    inputIngredientsViewModel.handleIntent(
                        InputIngredientsIntent.CheckingSelectedIngredient(ingredientName)
                    )
                },
                iconPlaceholder = R.drawable.add_24px,
                iconDescriptionPlaceholder = R.string.icon_add,
                placeholder = stringResource(R.string.placeholder_input_ingredients),
            )

            if (inputIngredientsViewModel.inputTextStateIngredient.text.isNotEmpty()){
                when(val sourceState = ingredientsNameState.value) {
                    is SourceState.Loading -> LoadingScreen()
                    is SourceState.Success -> ShowSuggestionsIngredients(
                        state = inputIngredientsViewModel.inputTextStateIngredient,
                        ingredientsName = sourceState.data.toPersistentList(),
                        onSuggestionClick = { suggestion: String ->
                            inputIngredientsViewModel.handleIntent(
                                InputIngredientsIntent.SelectSuggestionIngredient(suggestion)
                            )
                        },
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
            if (inputIngredientsViewModel.inputTextStateIngredient.text.isEmpty()) {
                when(val stateSource = inputtedIngredientsState.value) {
                    is SourceState.Loading -> LoadingScreen()
                    is SourceState.Success -> ShowInputtedIngredients(
                        inputtedIngredients = stateSource.data.toPersistentList(),
                        onDeleteClick = { ingredient: IngredientInputedModel ->
                            inputIngredientsViewModel.handleIntent(
                                InputIngredientsIntent.IsRemoveIngredient(ingredient)
                            )
                        },
                        onWeightClick = { ingredientId: Int, ingredientWeight: Int, ingredientName: String ->
                            inputIngredientsViewModel.handleIntent(
                                InputIngredientsIntent.ChangeWeightIngredient(
                                    ingredientId,
                                    ingredientWeight,
                                    ingredientName
                                )
                            )
                        },
                    )
                    is SourceState.Error -> {
                        isNewNotify = true
                        notifyMessage = if (stateSource.message != null) {
                            "${stringResource(R.string.hardware_error)}\n${stateSource.message}"
                        } else {
                            stringResource(R.string.unknown_error_occurred)
                        }
                        notifyState = NotifyState.DANGER
                    }
                }
            }
            if (inputIngredientsUiState.isDeleteIngredient) {
                CustomDialog(
                    onDismissRequest = { inputIngredientsViewModel.handleIntent(
                        InputIngredientsIntent.DismissRemoveIngredient) },
                    onConfirmation = { inputIngredientsViewModel.handleIntent(
                        InputIngredientsIntent.RemoveInputtedIngredient) },
                    textDialog = stringResource(R.string.delete_ingredient),
                    item = inputIngredientsUiState.deletingIngredientName,
                )
            }
            if (inputIngredientsUiState.isIngredientNew) {
                MakeNewIngredient(
                    onDismissRequest = { inputIngredientsViewModel.handleIntent(
                        InputIngredientsIntent.DismissNewIngredient) },
                    onConfirmation = { ingredientCategory: String ->
                        inputIngredientsViewModel.handleIntent(
                            InputIngredientsIntent.AddNewIngredient(ingredientCategory)
                        ) },
                    item = inputIngredientsUiState.newIngredientName,
                    listCategory = inputIngredientsUiState.categories.toPersistentList(),
                )
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