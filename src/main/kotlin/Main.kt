import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.knk190001.easyhook_java.LocalHook
import com.github.knk190001.fluentuicompose.generated.*
import controls.*
import interop.User32
import interop.enableMica
import interop.enableTransparency
import interop.setDarkModeState
import jnr.ffi.annotations.Delegate
import kotlinx.coroutines.delay
import java.awt.Color
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.plaf.metal.DefaultMetalTheme

@Composable
fun App() {
    Window(onCloseRequest = applicationScope::exitApplication) {
        Canvas(Modifier.size(400.dp)) {
            drawIntoCanvas {

            }
        }
    }

}

lateinit var windowGlobal: ComposeWindow
val composeWindow = compositionLocalOf<ComposeWindow> { error("Window not initialized") }
lateinit var applicationScope: ApplicationScope
fun main() {
//    CWH.createWindowHook()
    CreateWindowHook.hookCreateWindow()
    application {
        applicationScope = this
        TestApp()
    }
}
var visible by mutableStateOf(false)
@Composable
private fun TestApp() {
    var darkMode by remember {
        mutableStateOf(true)
    }

    val windowState = rememberWindowState(width = 1600.dp * .75f, height = 1200.dp * .75f)

    LaunchedEffect(Unit){
        delay(1)
        visible = true
    }
    Window(state = windowState, visible = visible, onCloseRequest = applicationScope::exitApplication) {
        LaunchedEffect(Unit){
            enableTransparency(window)
            enableMica(window,true);
            windowGlobal = window
        }
        CompositionLocalProvider(composeWindow provides window){
            FluentTheme(if (darkMode) dark else light, if (darkMode) darkGradient else lightGradient) {
                FluentSurface(Modifier.offset(50.dp, 50.dp), 4.dp, true) {
                    Column(Modifier.size(600.dp).padding(20.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                        FluentText("Hello world", color = FluentTheme.colors.fillColor.text.primary, font = fonts.title)
                        FluentAccentButton(
                            Modifier
                                .padding(top = 20.dp)
                                .size(100.dp, 40.dp),
                            onClick = {
                                darkMode = !darkMode
                                setDarkModeState(windowGlobal, darkMode)
                            }
                        ) { bs ->
                            FluentText("Button", color = bs.getOnAccentTextColor(), font = fonts.body)
                        }
                        FluentToggleButton(
                            Modifier
                                .padding(top = 20.dp)
                                .size(200.dp, 40.dp),
                            onToggle = {
                                if(it){
                                }
                            },
                        ) {
                            FluentText("ToggleButton", font = fonts.body)
                        }

                        FluentHyperlinkButton(
                            Modifier
                                .padding(top = 20.dp)
                                .size(200.dp, 40.dp),
                            onClick = {},
                        ) {
                            FluentText("HyperLinkButton", font = fonts.body)
                        }

                        FluentHyperlink(
                            Modifier.padding(top = 20.dp),
                            {},
                            "HyperlinkButton"
                        )

                        FluentDropDown(
                            Modifier
                                .padding(top = 20.dp)
                                .size(150.dp, 40.dp)
                            , parentWindow = composeWindow.current
                        ) {
                            FluentText("Dropdown",font = fonts.body)
                        }
                    }

                }
            }

        }
    }
}

