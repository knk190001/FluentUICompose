package interop

import androidx.compose.ui.awt.ComposeWindow
import jnr.ffi.LibraryLoader
import jnr.ffi.Pointer
import jnr.ffi.Runtime
import org.jetbrains.skiko.SkiaLayer
import javax.swing.JLayeredPane

interface DWM {
    fun DwmSetWindowAttribute(hwnd: Long, dwAttribute: DWMAttribute, pvAttribute: Pointer, cbAttribute: Int)
}

interface WinGDI{
    fun CreateRoundRectRgn(x1: Int, x2: Int, y1: Int, y2: Int, w: Int, h: Int): Long
}

interface WinUser{
    fun SetWindowRgn(hwnd: Long, hrgn: Long, redraw: Boolean)
}

val dwm = LibraryLoader.create(DWM::class.java).load("dwmapi")
val wingdi = LibraryLoader.create(WinGDI::class.java).load("gdi32")
val winuser = LibraryLoader.create(WinUser::class.java).load("user32")
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
fun enableMica(window: Long, darkMode: Boolean) {
    darkModeState.putInt(0, 0)
    if (darkMode) {
        darkModeState.putInt(0, 1)
        dwm.DwmSetWindowAttribute(
            window,
            DWMAttribute.DWMA_USE_IMMERSIVE_DARK_MODE,
            darkModeState,
            runtime.addressSize()
        )
    }
    pvAttrPointer.putInt(0, 2)
    dwm.DwmSetWindowAttribute(
        window,
        DWMAttribute.DWMWA_SYSTEMBACKDROP_TYPE,
        pvAttrPointer,
        runtime.addressSize()
    )
}

fun enableAcrylic(window: ComposeWindow) {
    pvAttrPointer.putInt(0, 3)
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMWA_SYSTEMBACKDROP_TYPE,
        pvAttrPointer,
        runtime.addressSize()
    )
}

fun enableRoundedCorners(window: ComposeWindow){
    pvAttrPointer.putInt(0,2)
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMWA_WINDOW_CORNER_PREFERENCE,
        pvAttrPointer,
        runtime.addressSize()
    )
}
fun disableTransitions(window: ComposeWindow){
    pvAttrPointer.putInt(0,1)
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMWA_WINDOW_CORNER_PREFERENCE,
        pvAttrPointer,
        runtime.addressSize()
    )
}

fun setDarkModeState(window: ComposeWindow, isDark: Boolean) {
    darkModeState.putInt(0, if (isDark) 1 else 0 )
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMA_USE_IMMERSIVE_DARK_MODE,
        darkModeState,
        runtime.addressSize()
    )
}
fun enableTransparency(window: ComposeWindow) {
    val composeLayer = window.contentPane.getComponent(0) as JLayeredPane
    val skiaLayer = composeLayer.getComponent(0) as SkiaLayer
    composeLayer.remove(0)
    skiaLayer.transparency = true
    composeLayer.add(skiaLayer,1)
}