package com.alexit.justrecipes.presentation.feature.ownrecipes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
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
import com.alexit.justrecipes.common.NotifySideEffect
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.presentation.components.CircleLoader
import com.alexit.justrecipes.presentation.components.CustomDivider
import com.alexit.justrecipes.presentation.components.CustomPopup
import com.alexit.justrecipes.presentation.components.TitlePanel
import com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel.MakeOwnRecipeIntent
import com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel.MakeOwnRecipeViewModel
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import com.alexit.justrecipes.utility.dpToPx
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MakeOwnRecipeScreen(
    makeOwnRecipeViewModel: MakeOwnRecipeViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val makeOwnRecipeUiState by makeOwnRecipeViewModel.uiState.collectAsStateWithLifecycle()
    val ingredientsOwnRecipeState =
        makeOwnRecipeViewModel.ingredientsOwnRecipeState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val recipeName =
        if (makeOwnRecipeUiState.nameRecipe == "") stringResource(R.string.name_new_recipe)
        else makeOwnRecipeUiState.nameRecipe

    val portion = makeOwnRecipeUiState.portion
    val duration = makeOwnRecipeUiState.duration

    val width = JustRecipesTheme.dimensions.widthInputTextField
    val colorDigitalDataField = JustRecipesTheme.colors.background4
    val contentPadding = JustRecipesTheme.dimensions.paddingFieldInput
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val styleStrIngredients = JustRecipesTheme.typography.text11
    val colorStrIngredients = JustRecipesTheme.colors.text6

    var isNewNotify by remember { mutableStateOf(false) }
    var notifyMessage by remember { mutableStateOf("") }
    var notifyState by remember { mutableStateOf(NotifyState.INFO) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        makeOwnRecipeViewModel.sideEffect.collectLatest { notify ->
            when (notify) {
                is NotifySideEffect.ShowNotify -> {
                    isNewNotify = true
                    notifyMessage =
                        "${notify.message.asString(context)}\n${notify.addition}".trimEnd()
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
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TitlePanel(
            text = stringResource(R.string.title_make_own_recipes),
            onLeftClick = onBackClick,
            textLeft = stringResource(R.string.go_back),
            onRightClick = {
                if (!makeOwnRecipeUiState.isSaved)
                    makeOwnRecipeViewModel.handleIntent(MakeOwnRecipeIntent.SaveOwnRecipe)
            },
            textRight = if (!makeOwnRecipeUiState.isSaved) stringResource(R.string.save) else ""
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .verticalScroll(scrollState)
                .width(width),
        ) {
            //-----Name-----
            NameTextField(
                state = makeOwnRecipeViewModel.inputNameRecipeState,
                onDoneClick = { name ->
                    makeOwnRecipeViewModel.handleIntent(
                        MakeOwnRecipeIntent.GetNameRecipe(name)
                    )
                },
                iconPlaceholder = R.drawable.edit_3,
                iconDescriptionPlaceholder = R.string.edit_item,
                placeholder = recipeName
            )
            //-----Portion-----
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorDigitalDataField,
                        shape = RoundedCornerShape(radiusShape)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DigitDataField(
                    state = makeOwnRecipeViewModel.inputPortionState,
                    text = stringResource(R.string.portion),
                    onDoneClick = { portion ->
                        makeOwnRecipeViewModel.handleIntent(
                            MakeOwnRecipeIntent.GetPortion(portion)
                        )
                    },
                    placeholder = portion,
                    unit = stringResource(R.string.person)
                )
                Divider()
                //-----Duration-----
                DigitDataField(
                    state = makeOwnRecipeViewModel.inputDurationState,
                    text = stringResource(R.string.duration),
                    onDoneClick = { duration ->
                        makeOwnRecipeViewModel.handleIntent(
                            MakeOwnRecipeIntent.GetDuration(duration)
                        )
                    },
                    placeholder = duration,
                    unit = stringResource(R.string.minute)
                )
            }
            //-----Ingredients-----
            Column(
                modifier = Modifier
                    .padding(top = contentPadding, bottom = contentPadding)
                    .fillMaxWidth()
            ) {
                val ingredientsStr = stringResource(R.string.ingredients)
                BasicText(
                    text = ingredientsStr,
                    style = styleStrIngredients,
                    color = { colorStrIngredients },
                )
                when (val stateSource = ingredientsOwnRecipeState.value) {
                    is SourceState.Loading -> LoadingScreen()
                    is SourceState.Success -> ShowIngredientsOwnRecipe(
                        ingredients = stateSource.data.toPersistentList(),
                        portion = portion,
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
        }
    }
}

@Composable
private fun Divider() {
    val dividerColor = JustRecipesTheme.colors.border2
    val dividerThickness = JustRecipesTheme.dimensions.borderThickness
    val dividerWidth = JustRecipesTheme.dimensions.widthDivider
    CustomDivider(
        color = dividerColor,
        thickness = dividerThickness.dpToPx(),
        startX = - (dividerWidth / 2).dpToPx(),
        endX = (dividerWidth / 2).dpToPx(),
        startY = 0f,
        endY = 0f
    )
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
