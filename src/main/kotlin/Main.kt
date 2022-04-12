import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import interop.enableMica

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    Surface(Modifier.size(200.dp), color = Color.LightGray.copy(0.5f)) {

    }


}

@Composable
fun FluentSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier,
        shape = RoundedCornerShape(9.dp),
        border = BorderStroke(1.dp,Color.Black),
        content = content
    )
}

fun main() = application {
    var text by remember { mutableStateOf("Hello, World!") }
    Window(onCloseRequest = ::exitApplication) {
        LaunchedEffect(Unit) {
            enableTransparency(window)
            enableMica(window)
        }

        FluentSurface(Modifier.size(200.dp)) {

        }
    }
}
