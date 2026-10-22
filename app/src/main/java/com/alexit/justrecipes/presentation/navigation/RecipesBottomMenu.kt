package com.alexit.justrecipes.presentation.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import com.alexit.justrecipes.presentation.components.CustomButton
import com.alexit.justrecipes.presentation.components.CustomButtonSelected
import com.alexit.justrecipes.utility.dpToPx
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun RecipesBottomMenu(
    navigationState: RecipesNavigationState,
    navigator: RecipesNavigator,
) {
    val sizeWidth = sizeWidth()
    DrawBottonDividers(
        color = JustRecipesTheme.colors.dividerBottomMenu,
        strokeWidth = JustRecipesTheme.dimensions.thicknessDividerBottomMenu.dpToPx(),
        startX = sizeWidth.dpToPx(),
        startY = JustRecipesTheme.dimensions.heightDividerBottomMenu.dpToPx(),
        heightMenu = JustRecipesTheme.dimensions.heightBottomMenu.dpToPx()
    )

    val selectedTab = navigationState.currentTab

    BOTTOM_MENU_ROUTES.forEach { (tab, items) ->
        if (selectedTab == tab) {
            CustomButtonSelected(
                iconResId = items.icon,
                iconDescription = items.iconDescription,
                sizeWidth = sizeWidth,
                sizeHeight = JustRecipesTheme.dimensions.heightBottomMenu,
                sizeIcon = JustRecipesTheme.dimensions.sizeIcon1,
                colorBottomMenuSelected = JustRecipesTheme.colors.bottomMenuSelected,
                colorOnBottomMenuSelected = JustRecipesTheme.colors.onBottomMenuSelected,
                colorTopBorderButtonSelected = JustRecipesTheme.colors.topBorderButtonSelected,
                heightTopBorderButtonSelected = JustRecipesTheme.dimensions.heightTopBorderButtonSelected
            )
        } else {
            CustomButton(
                iconResId = items.icon,
                iconDescription = items.iconDescription,
                sizeWidth = sizeWidth,
                sizeHeight = JustRecipesTheme.dimensions.heightBottomMenu,
                sizeIcon = JustRecipesTheme.dimensions.sizeIcon1,
                colorOnBottomMenu = JustRecipesTheme.colors.onBottomMenu,
                onClick = { navigator.navigateTo(tab) }
            )
        }
    }
}

@Composable
private fun sizeWidth () : Dp {
    val screenWidth = LocalWindowInfo.current.containerDpSize.width
    return screenWidth / 4
}

@Composable
private fun DrawBottonDividers(
    color: Color,
    strokeWidth: Float,
    startX: Float,
    startY: Float,
    heightMenu: Float
) {
    Canvas(modifier = Modifier) {
        drawLine(
            color = color,
            start = Offset(startX, - startY / 2),
            end = Offset(startX, startY / 2),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(2 * startX, - startY / 2),
            end = Offset(2 * startX, startY / 2),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(3 * startX, - startY / 2),
            end = Offset(3 * startX, startY / 2),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(0f, heightMenu / 2),
            end = Offset(4 * startX, heightMenu / 2),
            strokeWidth = strokeWidth
        )
    }
}