package interop

import androidx.compose.ui.awt.ComposeWindow
import jnr.ffi.LibraryLoader
import jnr.ffi.Pointer
import jnr.ffi.Runtime

interface DWM {
    fun DwmSetWindowAttribute(hwnd: Long, dwAttribute: DWMAttribute, pvAttribute: Pointer, cbAttribute: Int)
}

val dwm = LibraryLoader.create(DWM::class.java).load("dwmapi")
private val runtime = Runtime.getRuntime(dwm)
private val memoryManager = runtime.memoryManager
private val darkModeState = memoryManager.allocateDirect(runtime.addressSize())
private val pvAttrPointer = memoryManager.allocateDirect(runtime.addressSize())

fun enableMica(window: ComposeWindow, darkMode: Boolean) {

    darkModeState.putInt(0, 0)
    if (darkMode) {
        darkModeState.putInt(0, 1)
        dwm.DwmSetWindowAttribute(
            window.windowHandle,
            DWMAttribute.DWMA_USE_IMMERSIVE_DARK_MODE,
            darkModeState,
            runtime.addressSize()
        )
    }
    pvAttrPointer.putInt(0, 2)
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMWA_SYSTEMBACKDROP_TYPE,
        pvAttrPointer,
        runtime.addressSize()
    )
}

fun toggleDarkMode(window: ComposeWindow) {
    val current = darkModeState.getInt(0)
    darkModeState.putInt(0, if (current == 1) 0 else 1)
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMA_USE_IMMERSIVE_DARK_MODE,
        darkModeState,
        runtime.addressSize()
    )
}