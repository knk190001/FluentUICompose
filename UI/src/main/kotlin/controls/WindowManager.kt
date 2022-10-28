package controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import interop.*
import jnr.ffi.Pointer
import jnr.ffi.Runtime
import jnr.ffi.types.u_int32_t
import java.awt.Point

@Composable
fun ProvideWindowManager(content: @Composable () -> Unit) {
    val windowManager = WindowManager(WindowType.Main)
    windowManager.init()
    CompositionLocalProvider(LocalWindowManager provides windowManager, content = content)
}

@Composable
fun SetWindowType(newType: WindowType, content: @Composable () -> Unit) {
    val currentWindowType = LocalWindowManager.current.currentWindowType
    LocalWindowManager.current.currentWindowType = newType
    cwt = newType
    cwtSet = true
    content()
    LocalWindowManager.current.currentWindowType = currentWindowType
}

val LocalWindow = compositionLocalOf<ComposeWindow> {
    error("No main window provided")
}

@Composable
fun MainWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    visible: Boolean = true,
    title: String = "Untitled",
    icon: Painter? = null,
    undecorated: Boolean = false,
    transparent: Boolean = false,
    resizable: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    content: @Composable FrameWindowScope.() -> Unit
) {
    SetWindowType(WindowType.Main) {
        Window(
            onCloseRequest,
            state,
            visible,
            title,
            icon,
            undecorated,
            transparent,
            resizable,
            enabled,
            focusable,
            alwaysOnTop,
            onPreviewKeyEvent,
            onKeyEvent
        ) {
            CompositionLocalProvider(LocalWindow provides window) {
                content()
            }
        }
    }
}

@Composable
fun SetParentWindow(window: ComposeWindow, content: @Composable () -> Unit) {
    var currentParent = LocalWindowManager.current.parent
    LocalWindowManager.current.parent = window
    parentG = window
    content()
    LocalWindowManager.current.parent = currentParent
}

@Composable
fun EphemeralWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    visible: Boolean = true,
    title: String = "Untitled",
    icon: Painter? = null,
    undecorated: Boolean = false,
    transparent: Boolean = false,
    resizable: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    content: @Composable FrameWindowScope.() -> Unit
) {
    SetParentWindow(LocalWindow.current) {
        SetWindowType(WindowType.Ephemeral) {
            Window(
                onCloseRequest,
                state,
                visible,
                title,
                icon,
                undecorated,
                transparent,
                resizable,
                enabled,
                focusable,
                alwaysOnTop,
                onPreviewKeyEvent,
                onKeyEvent,
                content
            )
        }
    }
}

val LocalWindowManager = compositionLocalOf<WindowManager> {
    error("Must provide window manager")
}
var wm:WindowManager? = null
var parentG:ComposeWindow? = null
var cwt:WindowType = WindowType.Main
var cwtSet = false
class WindowManager(var currentWindowType: WindowType) {
    var parent: ComposeWindow? = null

    init {
        wm = this
    }

    fun init() {
        CreateWindowHook.hookCreateWindow { dwExStyle, lpClassName, lpWindowName, dwStyle, x, y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam ->
            val rt = Runtime.getRuntime(User32.user32Impl)

            val className = if (isTopLevelWindow(lpClassName) && cwtSet) cwt.className.toWCharArray(rt) else lpClassName
            val style = if(isTopLevelWindow(lpClassName)&& cwt == WindowType.Ephemeral) (dwStyle or 0x80000000L) else dwStyle

            val result = User32.CreateWindowExW(
                dwExStyle,
                className,
                lpWindowName,
                style,
                x,
                y,
                nWidth,
                nHeight,
                if (parentG != null) Pointer.wrap(rt,parentG!!.windowHandle) else hWndParent,
//                hWndParent,
                hMenu,
                hInstance,
                lpParam
            )
            if (isTopLevelWindow(lpClassName)) {
                if (parentG != null) {
                    parentG = null
                }
                if (cwtSet) {
                    cwt = WindowType.Main
                    cwtSet = false
                }
            }
            result
        }
        //CreateWindowHook.hookCreateWindow(::CreateWindowExProc)
    }
}

enum class WindowType(val className: String) {
    Main("SunAwtFrame"), Ephemeral("EphemeralFrame")
}

fun isTopLevelWindow(pointer: Pointer?): Boolean {
    return pointer!!.wCharArrayToString() == "SunAwtFrame"
}