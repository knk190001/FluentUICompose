package controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.*
import interop.*
import jnr.ffi.Pointer
import jnr.ffi.Runtime
import kotlinx.coroutines.delay
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import java.awt.event.WindowListener
import javax.swing.JFrame

@Composable
fun FluentDropdownMenu(size: DpSize, position: WindowPosition, close: () -> Unit,parentWindow:ComposeWindow, content: @Composable () -> Unit) {
    val windowState = rememberWindowState(
        position = position,
        size = size
    )
    Window(close, visible = true, state = windowState, undecorated = true, transparent = true,) {
        LaunchedEffect(Unit) {
            val rt = Runtime.getRuntime(U32.user32Impl)
            U32.SetParent(Pointer.wrap(rt, window.windowHandle), Pointer.wrap(rt, parentWindow.windowHandle))
            enableRoundedCorners(window)
            enableAcrylic(window)
            setDarkModeState(window, true)


//            (window as JFrame).addWindowFocusListener(object : WindowFocusListener {
//                override fun windowGainedFocus(e: WindowEvent?) {}
//
//                override fun windowLostFocus(e: WindowEvent?) {
//                    close()
//                }
//            })

        }
        content()
    }
}
