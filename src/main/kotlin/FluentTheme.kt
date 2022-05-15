import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import com.github.knk190001.fluentuicompose.FluentTheme
import com.github.knk190001.fluentuicompose.light

val LocalFluentTheme = compositionLocalOf { light }

@Composable
fun FluentTheme(theme: FluentTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalFluentTheme provides theme) {
        content()
    }
}

object FluentTheme {
    @get:Composable
    @get:ReadOnlyComposable
    val colors
        get() = LocalFluentTheme.current
}