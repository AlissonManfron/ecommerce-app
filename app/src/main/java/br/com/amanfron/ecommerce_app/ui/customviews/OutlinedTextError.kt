package br.com.amanfron.ecommerce_app.ui.customviews

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextError(isVisible: Boolean) {
    if (isVisible) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            color = Color.Red,
            fontFamily = FontFamily.Default,
            text = "Preencha este campo."
        )
    }
}