@file:Suppress("FunctionName")

package controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalTextColor = compositionLocalOf {
    Color(0xe6000000)
}

@Composable
fun SetLocalTextColor(color: Color, content: @Composable ()-> Unit){
    CompositionLocalProvider(LocalTextColor provides color){
        content()
    }
}