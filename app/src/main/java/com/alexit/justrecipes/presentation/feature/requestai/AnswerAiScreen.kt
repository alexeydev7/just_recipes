package com.alexit.justrecipes.presentation.feature.requestai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.domain.model.ai.RecipeAi
import com.alexit.justrecipes.domain.model.ai.ResponseAi
import com.alexit.justrecipes.presentation.components.CircleLoader
import com.alexit.justrecipes.presentation.components.CustomDivider
import com.alexit.justrecipes.presentation.components.CustomPopup
import com.alexit.justrecipes.common.NotifySideEffect
import com.alexit.justrecipes.presentation.components.TitlePanel
import com.alexit.justrecipes.utility.dpToPx
import com.alexit.justrecipes.presentation.feature.requestai.viewmodel.AnswerAiViewModel
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json

@Composable
fun AnswerAiScreen(
    answerAiViewModel: AnswerAiViewModel = hiltViewModel(),
    promptUser: String,
    onBackClick: () -> Unit
) {
    val answerAiUiState by answerAiViewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val promptSystem = stringResource(R.string.prompt_sys)

    val padding = JustRecipesTheme.dimensions.gap1

    var isNewNotify by remember { mutableStateOf(false) }
    var notifyMessage by remember { mutableStateOf("") }
    var notifyState by remember { mutableStateOf(NotifyState.INFO) }
    val context = LocalContext.current

    LaunchedEffect(promptUser) {
        answerAiViewModel.getRecipeAi(promptUser, promptSystem)
        answerAiViewModel.sideEffect.collectLatest { notify ->
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

    if (answerAiUiState.isRecipeAiOk && !answerAiUiState.isSaved) {
        answerAiViewModel.saveRecipeAi()
        answerAiViewModel.changeRecipeAiTask()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TitlePanel(
            text = stringResource(R.string.title_answer_ai),
            onLeftClick = onBackClick,
            textLeft = stringResource(R.string.go_back),
            onRightClick = { if (!answerAiUiState.isSaved) answerAiViewModel.changeRecipeAiTask() },
            textRight = if (!answerAiUiState.isSaved) stringResource(R.string.save) else ""
        )
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (answerAiUiState.responseAi == null) {
                LoadingScreen()
            } else {
                val answerAi: ResponseAi = answerAiUiState.responseAi!!
                val recipeAiStr: String? = answerAi.choices.firstOrNull()?.message?.content

                if (recipeAiStr != null) {
                    runCatching { Json.decodeFromString<RecipeAi>(recipeAiStr) }
                        .onSuccess { recipeAi ->
                            answerAiViewModel.changeRecipeAiState(recipeAi)
                            ShowRecipeAi(recipeAi)
                        }
                        .onFailure { error ->
                            val errorMessage =
                                "${stringResource(R.string.json_error)}\n${error.message}"
                            isNewNotify = true
                            notifyMessage = errorMessage
                            notifyState = NotifyState.DANGER

                        }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ShowRecipeAi(recipeAi: RecipeAi) {

    val colorText = JustRecipesTheme.colors.text4
    val styleName = JustRecipesTheme.typography.text6
    val styleText = JustRecipesTheme.typography.text1
    val styleDigit = JustRecipesTheme.typography.text8
    val padding = JustRecipesTheme.dimensions.gap1
    val widthIngredientName = JustRecipesTheme.dimensions.widthIngredientNameInRecipe
    val unit = stringResource(R.string.g)

    BasicText(
        modifier = Modifier
            .align(alignment = Alignment.CenterHorizontally),
        style = styleName,
        color = { colorText },
        text = recipeAi.name
    )

    Divider()

    Row(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val portionStr = stringResource(R.string.portion)
        val durationStr = stringResource(R.string.duration)
        BasicText(
            text = "$portionStr\n$durationStr",
            style = styleText,
            color = { colorText },
        )
        val portion = recipeAi.persons
        val duration = recipeAi.cookingTime
        val persons = stringResource(R.string.person)
        val minute = stringResource(R.string.minute)
        BasicText(
            text = "$portion $persons\n$duration $minute",
            style = styleDigit,
            color = { colorText },
        )
    }

    Divider()

    val ingredientsStr = stringResource(R.string.ingredients)
    BasicText(
        text = ingredientsStr,
        style = styleDigit,
        color = { colorText },
    )
    recipeAi.ingredients.forEach { ingredient ->
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicText(
                modifier = Modifier
                    .width(widthIngredientName),
                text = ingredient.name.replaceFirstChar { it.uppercase() },
                style = styleText.copy(textAlign = TextAlign.Left),
                color = { colorText },
            )
            BasicText(
                text = "${ingredient.quantity} $unit",
                style = styleDigit.copy(textAlign = TextAlign.Right),
                color = { colorText },
            )
        }
    }

    Divider()

    recipeAi.steps.forEachIndexed { i, step ->
        BasicText(
            modifier = Modifier
                .padding(top = padding)
                .fillMaxWidth(),
            text = "${i + 1}. $step",
            style = styleText.copy(textAlign = TextAlign.Justify),
            color = { colorText },
        )
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
private fun Divider() {
    val dividerColor = JustRecipesTheme.colors.border2
    val dividerThickness = JustRecipesTheme.dimensions.borderThickness
    val dividerWidth = JustRecipesTheme.dimensions.widthInputTextField
    CustomDivider(
        color = dividerColor,
        thickness = dividerThickness.dpToPx(),
        startX = - (dividerWidth / 2).dpToPx(),
        endX = (dividerWidth / 2).dpToPx(),
        startY = 0f,
        endY = 0f
    )
}