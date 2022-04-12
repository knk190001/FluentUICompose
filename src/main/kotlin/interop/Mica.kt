package interop

import androidx.compose.ui.awt.ComposeWindow
import jnr.ffi.LibraryLoader
import jnr.ffi.Pointer
import jnr.ffi.Runtime

interface DWM {
    fun DwmSetWindowAttribute(hwnd: Long, dwAttribute: DWMAttribute, pvAttribute: Pointer, cbAttribute: Int)
}


fun enableMica(window: ComposeWindow) {
    val dwm = LibraryLoader.create(DWM::class.java).load("dwmapi")
    val runtime = Runtime.getRuntime(dwm)
    val memoryManager = runtime.memoryManager

    val pvAttrPointer = memoryManager.allocateDirect(runtime.addressSize())
    pvAttrPointer.putInt(0, 1)

//    dwm.DwmSetWindowAttribute(
//        window.windowHandle,
//        DWMAttribute.DWMA_MICA_EFFECT,
//        pvAttrPointer,
//        runtime.addressSize()
//    )
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMA_USE_IMMERSIVE_DARK_MODE,
        pvAttrPointer,
        runtime.addressSize()
    )
    pvAttrPointer.putInt(0,2)
    dwm.DwmSetWindowAttribute(
        window.windowHandle,
        DWMAttribute.DWMWA_SYSTEMBACKDROP_TYPE,
        pvAttrPointer,
        runtime.addressSize()
    )
}