package interop

import com.github.knk190001.easyhook_java.LocalHook
import jnr.ffi.Pointer
import jnr.ffi.Runtime
import java.lang.StringBuilder




object CreateWindowHook {
    fun hookCreateWindow(hook: CreateWindowExWHook) {

        val cwexPtr = LocalHook.GetProcAddress("user32", "CreateWindowExW")
        val cwexHook = LocalHook.create(
            cwexPtr,
            hook,
            CreateWindowExWHook::class.java,
            null
        )
        cwexHook.setExclusiveACL(LongArray(0))
    }

    private fun isMainWindow(windowName: String, hwnd: Pointer?): Boolean {
        return windowName.isEmpty() && getWindowClassName(hwnd) == "SunAwtFrame" || getWindowClassName(hwnd) == "SunAwtCanvas"
    }

    private fun getWindowClassName(hwnd: Pointer?): String {
        val rt = Runtime.getRuntime(User32.user32Impl)
        val mm = rt.memoryManager
        val classNameBuffer = mm.allocate(64)
        User32.GetClassNameW(hwnd, classNameBuffer, 64)
        return lpStringToJavaString(classNameBuffer)
    }

    fun shortToChar(s: Short): Char {
        return s.toInt().toChar()
    }

    fun lpStringToJavaString(lpString: Pointer?): String {
        val sb = StringBuilder()
        var i: Long = 0
        while (lpString!!.getShort(i).toInt() != 0) {
            sb.append(shortToChar(lpString.getShort(i)))
            i += 2
        }
        return sb.toString()
    }
}