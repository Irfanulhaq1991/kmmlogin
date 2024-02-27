package com.irfan.composeexploration.ui.theme.theme3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable





object MyAppTheme {
    val colors: MyAppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: MyAppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}


