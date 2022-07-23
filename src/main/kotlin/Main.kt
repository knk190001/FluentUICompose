import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.*

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.knk190001.fluentuicompose.generated.*
import controls.*
import interop.enableMica
import interop.setDarkModeState

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    Surface(Modifier.size(200.dp), color = Color.LightGray.copy(0.5f)) {

    }


}
lateinit var windowGlobal: ComposeWindow
fun main() = application {
    var darkMode by remember {
        mutableStateOf(true)
    }

    Window(onCloseRequest = ::exitApplication) {
        LaunchedEffect(Unit) {
            enableTransparency(window)
            enableMica(window, darkMode)
            windowGlobal = window
        }
        FluentTheme(if (darkMode) dark else light, if (darkMode) darkGradient else lightGradient) {
            FluentSurface(Modifier.offset(50.dp, 50.dp), 4.dp, false) {
                Column(Modifier.size(600.dp).padding(20.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                    FluentText("Hello world", color = FluentTheme.colors.fillColor.text.primary, font = fonts.title)
                    FluentAccentButton  (
                        Modifier
                            .padding(top = 20.dp)
                            .size(100.dp,40.dp),
                        onClick = {
                            darkMode = !darkMode
                            setDarkModeState(windowGlobal, darkMode)
                        }
                    ) {
                        FluentText("Button", font = fonts.body)
                    }

                    FluentButton(
                        Modifier
                            .padding(top = 20.dp)
                            .size(100.dp,40.dp)
                    ) {
                        FluentText("Button", font = fonts.body)
                    }

                    FluentToggleButton(Modifier
                        .padding(top = 20.dp)
                        .size(150.dp,40.dp),
                        onToggle = {},
                    ){
                        FluentText("ToggleButton", font = fonts.body)
                    }

                    FluentHyperlinkButton(Modifier
                        .padding(top = 20.dp)
                        .size(150.dp,40.dp),
                        onClick = {},
                    ){
                        FluentText("ToggleButton", font = fonts.body)
                    }

                    FluentHyperlink(
                        Modifier.padding(top = 20.dp),
                        {},
                        "HyperlinkButton"
                    )
                }

            }
        }
    }
}

