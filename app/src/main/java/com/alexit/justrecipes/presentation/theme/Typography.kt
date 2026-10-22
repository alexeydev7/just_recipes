package com.alexit.justrecipes.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.alexit.justrecipes.R

val NunitoFont = FontFamily(
    Font(R.font.nunito_extra_light, weight = FontWeight.ExtraLight, style = FontStyle.Normal),
    Font(R.font.nunito_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(R.font.nunito_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.nunito_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(R.font.nunito_semi_bold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    Font(R.font.nunito_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.nunito_extra_bold, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
    Font(R.font.nunito_black, weight = FontWeight.Black, style = FontStyle.Normal),
)
@Immutable
data class CustomTypography(
    val text1: TextStyle,
    val text2: TextStyle,
    val text3: TextStyle,
    val text4: TextStyle,
    val text5: TextStyle,
    val text6: TextStyle,
    val text7: TextStyle,
    val text8: TextStyle,
    val text9: TextStyle,
    val text10: TextStyle,
    val text11: TextStyle
)
val themeTypography = CustomTypography(
    text1 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp
    ),
    text2 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    text3 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        textDecoration = TextDecoration.Underline
    ),
    text4 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    text5 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    text6 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    ),
    text7 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        textAlign = TextAlign.Left
    ),
    text8 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        textDecoration = TextDecoration.Underline,
        textAlign = TextAlign.Right
    ),
    text9 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    text10 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        textDecoration = TextDecoration.Underline
    ),
    text11 = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    )
)

val LocalCustomTypography = staticCompositionLocalOf {
    themeTypography
}