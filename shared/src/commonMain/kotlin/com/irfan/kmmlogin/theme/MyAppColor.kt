package com.irfan.composeexploration.ui.theme.theme3

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val Color_Light_Primary = Color(0XFFF5F5F5)
private val Color_Light_Secondary = Color(0xFFEEEEEE)
private val Color_Light_Background = Color(0xFFFFFFFF)
private val Color_Light_Background_Selected = Color(0xFFE0E0E0)
private val Color_Light_Text = Color(0xFF000000)
private val Color_Light_Drawer_Color = Color(0xFFFAFAFA)

private val Color_Dark_Primary = Color(0XFF424242)
private val Color_Dark_Secondary = Color(0xFF616161)
private val Color_Dark_Background = Color(0xFF000000)
private val Color_Dark_Background_Selected = Color(0XFF757575)
private val Color_Dark_Text = Color(0xFFFFFFFF)
private val Color_Dark_Drawer_Color = Color(0xFF212121)


class MyAppColors(
    primary: Color,
    secondary: Color,
    background: Color,
    backgroundSelected: Color,
    drawer: Color,
    text: Color,

    ) {
    var primary by mutableStateOf(primary)
    var secondary by mutableStateOf(secondary)
    var background by mutableStateOf(background)
    var backgroundSelected by mutableStateOf(backgroundSelected)
    var drawer by mutableStateOf(drawer)
    var text by mutableStateOf(text)


    fun copy(
        primary: Color = this.primary,
        secondary: Color = this.secondary,
        background: Color = this.background,
        backgroundSelected: Color = this.backgroundSelected,
        drawer:Color = this.drawer,
        text: Color = this.text,
    ): MyAppColors = MyAppColors(primary, secondary, background, backgroundSelected,drawer, text)

    fun updateFrom(other: MyAppColors) {
        primary = other.primary
        secondary = other.secondary
        background = other.background
        backgroundSelected = other.backgroundSelected
        this.drawer = other.drawer
        text = other.text
    }
}

fun lightColors(
    primary: Color = Color_Light_Primary,
    secondary: Color = Color_Light_Secondary,
    background: Color = Color_Light_Background,
    backgroundSelected: Color = Color_Light_Background_Selected,
    drawer: Color = Color_Light_Drawer_Color,
    text: Color = Color_Light_Text,
): MyAppColors = MyAppColors(primary, secondary, background, backgroundSelected,drawer, text)

fun darkColors(
    primary: Color = Color_Dark_Primary,
    secondary: Color = Color_Dark_Secondary,
    background: Color = Color_Dark_Background,
    backgroundSelected: Color = Color_Dark_Background_Selected,
    drawer: Color = Color_Dark_Drawer_Color,
    text: Color = Color_Dark_Text,
): MyAppColors = MyAppColors(primary, secondary, background, backgroundSelected,drawer, text)


internal val LocalColors = staticCompositionLocalOf { lightColors() }
