package com.alexit.justrecipes.utility

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
internal fun Dp.dpToPx(): Float {
    return this.value * LocalDensity.current.density
}