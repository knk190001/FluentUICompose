package controls

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import com.github.knk190001.fluentuicompose.generated.FluentColors
import com.github.knk190001.fluentuicompose.generated.FluentGradients
import com.github.knk190001.fluentuicompose.generated.light
import com.github.knk190001.fluentuicompose.generated.lightGradient

val LocalFluentTheme = compositionLocalOf { light }
val LocalFluentGradient = compositionLocalOf { lightGradient }

private object NoIndication : Indication {
    private object NoIndicationInstance : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        return NoIndicationInstance
    }
}

@Composable
fun FluentTheme(theme: FluentColors, gradients: FluentGradients, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalFluentTheme provides theme, LocalFluentGradient provides gradients, LocalIndication provides NoIndication) {
        content()
    }
}

object FluentTheme {
    val colors
        @ReadOnlyComposable
        @Composable
        get() = LocalFluentTheme.current

    val gradients
        @ReadOnlyComposable
        @Composable
        get() = LocalFluentGradient.current
}