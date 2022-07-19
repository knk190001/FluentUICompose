package controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color


val LocalColorStack = compositionLocalOf<Color> {
    error("Must be called inside StartColorStack")
}

@Composable
fun StartColorStack(baseColor: Color, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalColorStack provides baseColor) {
        content()
    }
}

@Composable
fun AddColorToStack(new: Color, content: @Composable () -> Unit) {
    val current = LocalColorStack.current
    val composited = compositeColors(new, current)
    CompositionLocalProvider(LocalColorStack provides composited) {
        content()
    }
}

fun compositeColors(c0: Color, c1: Color): Color {
    val (r0, g0, b0, a0) = c0
    val (r1, g1, b1, a1) = c1

    val a01 = (1 - a0) * a1 + a0

    return Color(
        ((1 - a0) * a1 * r1 + a0 * r0) / a01,
        ((1 - a0) * a1 * g1 + a0 * g0) / a01,
        ((1 - a0) * a1 * b1 + a0 * b0) / a01,
        a01
    )
}