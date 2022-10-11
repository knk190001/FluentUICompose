package controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import interop.CreateWindowExWHook
import interop.CreateWindowHook
import interop.User32
import jnr.ffi.Pointer
import jnr.ffi.types.u_int32_t
import java.awt.Point

@Composable
fun ProvideWindowManager(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalWindowManager provides WindowManager(),content = content)
}

val LocalWindowManager = compositionLocalOf<WindowManager> {
    error("Must provide window manager")
}

class WindowManager(var transform: CreateWindowTransform = IdentityTransform()) {
    init {
        CreateWindowHook.hookCreateWindow { dwExStyle, lpClassName, lpWindowName, dwStyle, x, y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam ->
//            transform.transform(User32::CreateWindowExW).invoke(dwExStyle, lpClassName, lpWindowName, dwStyle, x, y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam)
            User32.CreateWindowExW(dwExStyle, lpClassName, lpWindowName, dwStyle, x, y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam)
        }
    }
}

interface CreateWindowTransform {
    fun transform(hook: CreateWindowExWHook): CreateWindowExWHook
}

class IdentityTransform : CreateWindowTransform {
    override fun transform(hook: CreateWindowExWHook): CreateWindowExWHook {
        return hook
    }

}