package com.irfan.composeexploration.ui.theme.theme3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember


@Composable
fun AppTheme(
    colors: MyAppColors = AppTheme.colors,
    typography: MyAppTypography = AppTheme.typography,
    content: @Composable () -> Unit
) {
    val rememberColors = remember {
        colors.copy()
    }.apply { updateFrom(colors) }

    CompositionLocalProvider(LocalColors provides colors, LocalTypography provides typography, content = content)
}

object AppTheme {
    val colors: MyAppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: MyAppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}


