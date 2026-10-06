package com.alexit.justrecipes.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val green1 = Color(0xFFE8E8D6)
val green2 = Color(0xFF84B444)
val green3 = Color(0xFF83A656)
val green4 = Color(0x99DCEDD3)
val green5 = Color(0xFFDCEDD3)
val green6 = Color(0x33102107)
val green7 = Color(0xF29ADB46)
val green8 = Color(0xFF93A654)
val green9 = Color(0xFFCBD8A9)
val green10 = Color(0xFFB2C485)
val green11 = Color(0xFF658454)
val green12 = Color(0xFF70BB0E)
val green13 = Color(0xFF6C8B46)
val grey1 = Color(0x1A3E5134)
val black1 = Color(0xFF323230)
val black2 = Color(0xCCFFFFFF)
val brown1 = Color(0xFFC8BC9A)
val brown2 = Color(0x99AA9669)
val brown3 = Color(0xFFE5E2CE)
val brown4 = Color(0xFFAA9669)
val brown5 = Color(0x88AA9669)
val brown6 = Color(0xFF6B5B57)
val red1 = Color(0xFFEE5C4C)
val red2 = Color(0xF2FC746F)
val yellow1 = Color(0xF2E8E348)

@Immutable
data class CustomColors(
    val background0: Color,
    val background1: Color,
    val text1: Color,
    val dividerBottomMenu: Color,
    val bottomMenu: Color,
    val onBottomMenu: Color,
    val bottomMenuSelected: Color,
    val onBottomMenuSelected: Color,
    val topBorderButtonSelected: Color,
    val background2: Color,
    val border2: Color,
    val text2: Color,
    val background3: Color,
    val border3: Color,
    val text3: Color,
    val background4: Color,
    val text4: Color,
    val text5: Color,
    val text6: Color,
    val text7: Color,
    val iconSearchIngredient: Color,
    val iconDeleteIngredient: Color,
    val iconEdit: Color,
    val notifyBackgroundInfo: Color,
    val notifyBorderInfo: Color,
    val notifyTextInfo: Color,
    val notifyBackgroundWarning: Color,
    val notifyBorderWarning: Color,
    val notifyTextWarning: Color,
    val notifyBackgroundAlert: Color,
    val notifyBorderAlert: Color,
    val notifyTextAlert: Color,
    val circleLoader: Color,
    val iconOk: Color,
    val scrollBarColor: Color,
    val inputtedIngredients: Color
)

val themeColors = CustomColors(
    background0 = green1,
    background1 = green2,
    text1 = green5,
    dividerBottomMenu = grey1,
    bottomMenu = green3,
    onBottomMenu = green4,
    bottomMenuSelected = green6,
    onBottomMenuSelected = green5,
    topBorderButtonSelected = green7,
    background2 = black2,
    border2 = brown1,
    text2 = black1,
    background3 = green9,
    border3 = green10,
    text3 = green8,
    background4 = brown3,
    text4 = brown4,
    text5 = brown5,
    text6 = green11,
    text7 = brown2,
    iconSearchIngredient = brown2,
    iconDeleteIngredient = red1,
    iconEdit = green13,
    notifyBackgroundInfo = green7,
    notifyBorderInfo = grey1,
    notifyTextInfo = brown6,
    notifyBackgroundWarning = yellow1,
    notifyBorderWarning = grey1,
    notifyTextWarning = brown6,
    notifyBackgroundAlert = red2,
    notifyBorderAlert = grey1,
    notifyTextAlert = black1,
    circleLoader = green7,
    iconOk = green7,
    scrollBarColor = brown4,
    inputtedIngredients = green12
)

val LocalCustomColors = staticCompositionLocalOf {
    themeColors
}