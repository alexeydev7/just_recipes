package com.alexit.justrecipes.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.alexit.justrecipes.R
import com.alexit.justrecipes.utility.dpToPx
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    textDialog: String,
    item: String,
) {
    val heightDialog = JustRecipesTheme.dimensions.heightDialog
    val widthDialog = JustRecipesTheme.dimensions.widthDialog
    val colorBackground = JustRecipesTheme.colors.background4
    val colorBorder = JustRecipesTheme.colors.text4
    val colorText = JustRecipesTheme.colors.text4
    val radiusShape = JustRecipesTheme.dimensions.radiusCornerField
    val borderThickness = JustRecipesTheme.dimensions.borderThickness
    val textStyle = JustRecipesTheme.typography.text1
    val textConfirmation = stringResource(R.string.confirmation)
    val textDismiss = stringResource(R.string.dismiss)

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .size(
                    height = heightDialog,
                    width = widthDialog
                )
                .background(
                    color = colorBackground,
                    shape = RoundedCornerShape(size = radiusShape)
                )
                .border(
                    border = BorderStroke(
                        width = borderThickness,
                        color = colorBorder
                    ),
                    shape = RoundedCornerShape(size = radiusShape)
                ),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            BasicText(
                modifier = Modifier
                    .weight(
                        fill = true,
                        weight = 1f
                    )
                    .wrapContentHeight()
                    .fillMaxWidth(),
                style = textStyle.copy(
                    textAlign = TextAlign.Center
                ),
                color = { colorText },
                text = "$textDialog\n\r$item?"
            )

            Row(
                modifier = Modifier
                    .height(heightDialog / 4)
                    .border(
                        border = BorderStroke(
                            width = borderThickness,
                            color = colorBorder
                        ),
                        shape = RoundedCornerShape(
                            bottomStart = radiusShape,
                            bottomEnd = radiusShape
                        )
                    ),
                    //.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = radiusShape))
                        .clickable(
                            enabled = true,
                            onClick = onDismissRequest
                        )
                        .width(widthDialog / 2)
                        .fillMaxHeight()
                        .wrapContentHeight(),
                    style = textStyle.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = { colorText },
                    text = textDismiss
                )

                CustomDivider(
                    color = colorBorder,
                    thickness = borderThickness.dpToPx(),
                    startX = 0f,
                    endX = 0f,
                    startY = ((heightDialog / 4) / 2).dpToPx(),
                    endY = - ((heightDialog / 4) / 2).dpToPx()
                )

                BasicText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = radiusShape))
                        .clickable(
                            enabled = true,
                            onClick = onConfirmation
                        )
                        .width(widthDialog / 2)
                        .fillMaxHeight()
                        .wrapContentHeight(),
                    style = textStyle.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = { colorText },
                    text = textConfirmation
                )
            }
        }
    }
}
