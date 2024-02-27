package com.irfan.composeexploration.ui.theme.theme3

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class MyAppTypography(
    val label: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        fontSize = 16.sp,

    ),
    val body: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 12.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { MyAppTypography() }