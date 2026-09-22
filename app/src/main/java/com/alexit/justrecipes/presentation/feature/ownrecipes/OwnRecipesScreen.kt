package com.alexit.justrecipes.presentation.feature.ownrecipes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.common.SourceState
import com.alexit.justrecipes.domain.model.database.RecipeIdNameModel
import com.alexit.justrecipes.presentation.components.CircleLoader
import com.alexit.justrecipes.presentation.components.CustomPopup
import com.alexit.justrecipes.presentation.components.CustomTextField
import com.alexit.justrecipes.presentation.components.TitlePanel
import com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel.OwnRecipesIntent
import com.alexit.justrecipes.presentation.feature.ownrecipes.viewmodel.OwnRecipesViewModel
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun OwnRecipesScreen(
    ownRecipesViewModel: OwnRecipesViewModel = hiltViewModel(),
    onAddRecipeClick: () -> Unit
) {
    val ownRecipesUiState by ownRecipesViewModel.uiState.collectAsStateWithLifecycle()
    val ownRecipesIdNameState = ownRecipesViewModel.ownRecipesIdNameState.collectAsStateWithLifecycle()

    val paddingFieldInput = JustRecipesTheme.dimensions.paddingFieldInput

    var isNewNotify by remember { mutableStateOf(false) }
    var notifyMessage by remember { mutableStateOf("") }
    var notifyState by remember { mutableStateOf(NotifyState.INFO) }

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
            text = stringResource(R.string.title_own_recipes),
            onRightClick = onAddRecipeClick,
            textRight = stringResource(R.string.add_recipe)
        )
        Column(
            modifier = Modifier
                .padding(vertical = paddingFieldInput)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                state = ownRecipesViewModel.inputTextState,
                iconPlaceholder = R.drawable.text_search,
                iconDescriptionPlaceholder = R.string.icon_text_search,
                placeholder = stringResource(R.string.placeholder_search_recipes)
            )

            when (val sourceState = ownRecipesIdNameState.value) {
                is SourceState.Loading -> LoadingScreen()
                is SourceState.Success -> ShowOwnRecipes(
                    listOwnRecipes = sourceState.data.toPersistentList(),
                    onDeleteClick = { recipe: RecipeIdNameModel ->
                        ownRecipesViewModel.handleIntent(
                            OwnRecipesIntent.IsRemoveOwnRecipe(recipe)
                        )
                    },
                    onViewRecipe = { recipe: RecipeIdNameModel ->
                        ownRecipesViewModel.handleIntent(
                            OwnRecipesIntent.ViewRecipe(recipe)
                        )
                    },
                    onEditClick = { recipe: RecipeIdNameModel ->
                        ownRecipesViewModel.handleIntent(
                            OwnRecipesIntent.EditRecipe(recipe)
                        )
                    }
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
private fun ShowOwnRecipes(
    listOwnRecipes: PersistentList<RecipeIdNameModel>,
    onDeleteClick: (RecipeIdNameModel) -> Unit,
    onViewRecipe: (RecipeIdNameModel) -> Unit,
    onEditClick: (RecipeIdNameModel) -> Unit
) {
    val iconDeleteRecipe = R.drawable.round_do_not_disturb_on_24
    val descriptionIconDeleteRecipe = R.string.delete_item
    val iconEditRecipe = R.drawable.pencil_outline
    val descriptionIconEditRecipe = R.string.edit_item
    val colorIconDeleteRecipe = JustRecipesTheme.colors.iconDeleteIngredient
    val colorInputtedIngredientsField = JustRecipesTheme.colors.background4
    val colorRecipeNameText = JustRecipesTheme.colors.text4
    val textStyleRecipeName = JustRecipesTheme.typography.text1
    val contentPadding = JustRecipesTheme.dimensions.contentPaddingField
    val widthInputTextField = JustRecipesTheme.dimensions.widthInputTextField
    val bottomMenuHeight = JustRecipesTheme.dimensions.heightBottomMenu
    val widthRecipeNameField = JustRecipesTheme.dimensions.widthShowedTextField
    val sizeIcon = JustRecipesTheme.dimensions.sizeIcon1
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    LazyColumn(
        modifier = Modifier
            .consumeWindowInsets(paddingValues = PaddingValues(bottomMenuHeight))
            .imePadding()
            .animateContentSize()
    ) {
        items(items = listOwnRecipes, key = { it.id }) { recipe ->
            Row(
                modifier = Modifier
                    .width(widthInputTextField)
                    .padding(top = contentPadding)
                    .animateItem(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier
                        .size(sizeIcon)
                        .clickable(
                            enabled = true,
                            onClick = { onDeleteClick(recipe) }
                        ),
                    imageVector = ImageVector.vectorResource(iconDeleteRecipe),
                    contentDescription = stringResource(descriptionIconDeleteRecipe),
                    colorFilter = ColorFilter.tint(colorIconDeleteRecipe)
                )
                Row(
                    modifier = Modifier
                        .width(widthRecipeNameField)
                        .wrapContentHeight()
                        .background(
                            color = colorInputtedIngredientsField,
                            shape = RoundedCornerShape(radiusShape)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicText(
                        modifier = Modifier
                            .padding(start = contentPadding)
                            .clickable(
                                enabled = true,
                                onClick = { onViewRecipe(recipe) }
                            )
                            .weight(1f),
                            //.width(widthRecipeNameText),
                        style = textStyleRecipeName,
                        color = { colorRecipeNameText },
                        text = recipe.name
                    )
                    Image(
                        modifier = Modifier
                            .padding(end = contentPadding)
                            .size(sizeIcon)
                            .clickable(
                                enabled = true,
                                onClick = { onEditClick(recipe) }
                            ),
                        imageVector = ImageVector.vectorResource(iconEditRecipe),
                        contentDescription = stringResource(descriptionIconEditRecipe),
                        colorFilter = ColorFilter.tint(colorRecipeNameText)
                    )
                }
            }
        }
    }
}
