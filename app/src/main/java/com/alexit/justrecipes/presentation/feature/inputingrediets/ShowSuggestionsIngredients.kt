package com.alexit.justrecipes.presentation.feature.inputingrediets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexit.justrecipes.common.SuggestionsState
import com.alexit.justrecipes.common.customDebounce
import com.alexit.justrecipes.presentation.components.CustomDivider
import com.alexit.justrecipes.utility.dpToPx
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@Composable
fun ShowSuggestionsIngredients(
    state: TextFieldState,
    ingredientsName: PersistentList<String>,
    onSuggestionClick: (String) -> Unit,
){
    val width = JustRecipesTheme.dimensions.widthInputTextField
    val textStyle = JustRecipesTheme.typography.text2
    val colorField = JustRecipesTheme.colors.background2
    val colorBorderField = JustRecipesTheme.colors.border2
    val colorText = JustRecipesTheme.colors.text2
    val colorSuggestion = JustRecipesTheme.colors.background3
    val contentPadding = JustRecipesTheme.dimensions.contentPaddingField
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val borderThickness = JustRecipesTheme.dimensions.borderThickness
    val bottomMenuHeight = JustRecipesTheme.dimensions.heightBottomMenu

    val suggestionsState = rememberSuggestionsState(ingredientsName)
    val suggestions = suggestionsState.suggestions.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .consumeWindowInsets(paddingValues = PaddingValues(bottomMenuHeight))
            .imePadding()
            .width(width)
            .background(
                color = colorField,
                shape = RoundedCornerShape(size = radiusShape)
            )
            .border(
                border = BorderStroke(
                    width = borderThickness,
                    color = colorBorderField
                ),
                shape = RoundedCornerShape(size = radiusShape)
            )
            .padding(start = contentPadding, end = contentPadding)
            .animateContentSize(),
        verticalArrangement = Arrangement.Center
    ) {
        items(items = suggestions.value, key = { it.text }) { suggestion ->
            BasicText(
                modifier = Modifier
                    .padding(top = contentPadding, bottom = contentPadding)
                    .clickable(
                        enabled = true,
                        onClick = { onSuggestionClick(suggestion.text) }
                    ),
                text = suggestion,
                style = textStyle,
                color = { colorText }
            )
            CustomDivider(
                color = colorText,
                thickness = borderThickness.dpToPx(),
                startX = 0f,
                endX = (width - contentPadding * 2).dpToPx(),
                startY = 0f,
                endY = 0f
            )
        }
    }
    LaunchedEffect(state.text) {
        snapshotFlow { state.text }
            .customDebounce(300)
            //.debounce(300)
            .distinctUntilChanged()
            .collectLatest { query ->
                withContext(Dispatchers.IO) {
                    suggestionsState.suggestions.update {
                        highlight(
                            color = colorSuggestion,
                            query = query.toString().trim(),
                            items = suggestionsState.items,
                            matches = suggestionsState.findMatchingIndex(
                                query = query.toString().trim()
                            )
                        )
                    }
                }
            }
    }
}

@Composable
private fun rememberSuggestionsState(items: PersistentList<String>): SuggestionsState {
    val scope = rememberCoroutineScope()
    return remember { SuggestionsState(scope, items) }
}

private fun highlight(
    color: Color,
    query: String,
    items: List<String>,
    matches: Map<Int, List<Int>>
): List<AnnotatedString> {
    if (query.isEmpty()) return listOf()
    return matches
        .map { entry ->     // build list of annotated string from the index and character positions
            buildAnnotatedString {
                append(items[entry.key])
                entry.value.forEach {
                    addStyle(
                        style = SpanStyle(background = color),
                        start = it,
                        end = it + query.length
                    )
                }
            }
        }
}