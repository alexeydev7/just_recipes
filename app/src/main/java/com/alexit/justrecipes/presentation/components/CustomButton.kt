package com.alexit.justrecipes.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import com.alexit.justrecipes.utility.dpToPx

@Composable
fun CustomButton(
    iconResId: Int,
    iconDescription: Int,
    sizeWidth: Dp,
    sizeHeight: Dp,
    sizeIcon: Dp,
    colorOnBottomMenu: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(sizeHeight)
            .width(sizeWidth)
            .clickable(
                enabled = true,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(sizeIcon),
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = stringResource(id = iconDescription),
            colorFilter = ColorFilter.tint(colorOnBottomMenu)
        )
    }
}

@Composable
fun CustomButtonSelected(
    iconResId: Int,
    iconDescription: Int,
    sizeWidth: Dp,
    sizeHeight: Dp,
    sizeIcon: Dp,
    colorBottomMenuSelected: Color,
    colorOnBottomMenuSelected: Color,
    colorTopBorderButtonSelected: Color,
    heightTopBorderButtonSelected: Dp

) {
    Box(
        modifier = Modifier
            .height(sizeHeight)
            .width(sizeWidth)
            .background(colorBottomMenuSelected)
            .topBorder(
                color = colorTopBorderButtonSelected,
                height = heightTopBorderButtonSelected.dpToPx()
            )
            .clickable(
                enabled = false,
                onClick = {}
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(sizeIcon),
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = stringResource(id = iconDescription),
            colorFilter = ColorFilter.tint(colorOnBottomMenuSelected)
        )
    }
}

internal fun Modifier.topBorder(
    color: Color,
    height: Float,
) = this.drawWithContent {
    drawContent()
    drawLine(
        color = color,
        start = Offset(0f, - height/2),
        end = Offset(size.width, - height/2),
        strokeWidth = height,
    )
}
