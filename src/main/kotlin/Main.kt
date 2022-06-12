import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.knk190001.fluentuicompose.generated.*
import controls.FluentTheme
import interop.enableMica
import interop.toggleDarkMode

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    Surface(Modifier.size(200.dp), color = Color.LightGray.copy(0.5f)) {

    }


}

@Composable
fun FluentText(text: String, modifier: Modifier = Modifier, color: Color, font: SerializedFont) {
    Text(
        text,
        modifier,
        color,
        fontSize = font.fontSize.sp,
        textDecoration = TextDecoration.None,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(font.fontWeight),
        fontStyle = FontStyle.Normal, //TODO: Fix this
        letterSpacing = font.letterSpacing.sp,
        lineHeight = font.lineHeight.sp,
        //TODO: Add paragraph indent, paragraph spacing, and text case
    )
}

lateinit var windowGlobal: ComposeWindow
fun main() = application {
    var darkMode by remember {
        mutableStateOf(true)
    }

    var text by remember { mutableStateOf("Hello, World!") }
    Window(onCloseRequest = ::exitApplication) {
        LaunchedEffect(Unit) {
            enableTransparency(window)
            enableMica(window, darkMode)
            windowGlobal = window
        }
        FluentTheme(if (darkMode) dark else light, if (darkMode) darkGradient else lightGradient) {
            FluentSurface(Modifier.offset(50.dp, 50.dp), 4.dp) {
                Column(Modifier.padding(20.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                    FluentText("Hello world", color = FluentTheme.colors.fillColor.text.primary, font = fonts.title)
                    FluentButton(
                        onClick = {
                            darkMode = !darkMode
                            toggleDarkMode(windowGlobal)
                        }
                    ) { bs ->
                        FluentText("Button", color = bs.getTextColor(), font = fonts.body)
                    }
                }

            }

        }
    }
}

