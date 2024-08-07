package br.com.amanfron.ecommerce_app.ui.customviews

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf

@Stable
class NavigationState(
    val selectedItemIndex: MutableState<Int> = mutableIntStateOf(0)
)