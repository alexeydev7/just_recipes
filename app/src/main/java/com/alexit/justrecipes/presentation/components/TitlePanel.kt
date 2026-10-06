package com.alexit.justrecipes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.alexit.justrecipes.presentation.theme.JustRecipesTheme

@Composable
fun TitlePanel(
    text: String,
    onLeftClick: () -> Unit = {},
    textLeft: String = "",
    onRightClick: () -> Unit = {},
    textRight: String = "",
) {
    val height = JustRecipesTheme.dimensions.heightTitlePanel
    val background = JustRecipesTheme.colors.background1
    val colorTitleText = JustRecipesTheme.colors.text1
    val padding = JustRecipesTheme.dimensions.paddingTextTitlePanel
    val styleTitleText = JustRecipesTheme.typography.text1
    val styleButtonText = JustRecipesTheme.typography.text9
    val heightButton = JustRecipesTheme.dimensions.heightButtonTitlePanel

    Box(
        modifier = Modifier
            .height(height)
            .background(background)
            .padding(padding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        if (textLeft != "") {
            BasicText(
                modifier = Modifier
                    .clip(RoundedCornerShape(heightButton))
                    .clickable(
                        enabled = true,
                        onClick = onLeftClick
                    )
                    .align(Alignment.BottomStart)
                    .height(heightButton)
                    .background(colorTitleText)
                    .padding(
                        start = padding,
                        end=padding
                    )
                    .wrapContentHeight(align = Alignment.CenterVertically),
                style = styleButtonText.copy(textAlign = TextAlign.Center),
                color = { background },
                text = textLeft
            )
        }

        BasicText(
            modifier = Modifier
                //.align(Alignment.Center)
                .height(height)
                .wrapContentHeight(align = Alignment.Bottom),
            text = text,
            style = styleTitleText.copy(textAlign = TextAlign.Center),
            color = { colorTitleText },
        )
        if (textRight != "") {
            BasicText(
                modifier = Modifier
                    .clip(RoundedCornerShape(heightButton))
                    .clickable(
                        enabled = true,
                        onClick = onRightClick
                    )
                    .align(Alignment.BottomEnd)
                    .height(heightButton)
                    .background(colorTitleText)
                    .padding(
                        start = padding,
                        end=padding
                    )
                    .wrapContentHeight(align = Alignment.CenterVertically),
                style = styleButtonText.copy(textAlign = TextAlign.Center),
                color = { background },
                text = textRight
            )
        }

    }
}
