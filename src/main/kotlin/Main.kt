import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.knk190001.fluentuicompose.dark
import com.github.knk190001.fluentuicompose.light
import interop.enableMica

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    Surface(Modifier.size(200.dp), color = Color.LightGray.copy(0.5f)) {

    }


}



fun main() = application {
    val darkMode = true
    MaterialTheme
    var text by remember { mutableStateOf("Hello, World!") }
    Window(onCloseRequest = ::exitApplication) {
        LaunchedEffect(Unit) {
            enableTransparency(window)
            enableMica(window, darkMode)
        }
        FluentTheme(if (darkMode) dark else light) {
            FluentSurface(Modifier.offset(50.dp, 50.dp).size(200.dp)) {

            }

        }
    }
}

