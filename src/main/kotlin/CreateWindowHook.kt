import com.github.knk190001.easyhook_java.LocalHook
import interop.U32
import jnr.ffi.Pointer
import jnr.ffi.Runtime
import jnr.ffi.types.u_int32_t
import java.lang.StringBuilder

object CreateWindowHook {
    fun hookCreateWindow() {
        val cwexPtr = LocalHook.GetProcAddress("user32", "CreateWindowExW")
        val cwexHook = LocalHook.create(
            cwexPtr,
            CreateWindowExWHook { dwExStyle, lpClassName, lpWindowName, dwStyle, x, y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam ->
                createWindowExWRepl(
                    dwExStyle,
                    lpClassName,
                    lpWindowName,
                    dwStyle,
                    x,
                    y,
                    nWidth,
                    nHeight,
                    hWndParent,
                    hMenu,
                    hInstance,
                    lpParam
                )
            },
            CreateWindowExWHook::class.java,
            null
        )
        cwexHook.setExclusiveACL(LongArray(0))
    }

    fun createWindowExWRepl(
        dwExStyle: Long,
        lpClassName: Pointer?,
        lpWindowName: Pointer?,
        dwStyle: Long,
        x: Int,
        y: Int,
        nWidth: Int,
        nHeight: Int,
        hWndParent: Pointer?,
        hMenu: Pointer?,
        hInstance: Pointer?,
        lpParam: Pointer?
    ): Pointer? {
        val hwnd = U32.CreateWindowExW(
            dwExStyle,
            lpClassName,
            lpWindowName,
            dwStyle,
            x,
            y,
            nWidth,
            nHeight,
            hWndParent,
            hMenu,
            hInstance,
            lpParam
        )
//        if (lpWindowName !=null && isMainWindow(lpStringToJavaString(lpWindowName), hwnd)) {
//            println("Name: " + lpStringToJavaString(lpWindowName))
//            println("Class: " + getWindowClassName(hwnd))
//            println("HWND: $hwnd")
//            //MicaKt.enableMica(hwnd.address(),true);
//        }
        return hwnd
    }

    private fun isMainWindow(windowName: String, hwnd: Pointer?): Boolean {
        return windowName.isEmpty() && getWindowClassName(hwnd) == "SunAwtFrame" || getWindowClassName(hwnd) == "SunAwtCanvas"
    }

    private fun getWindowClassName(hwnd: Pointer?): String {
        val rt = Runtime.getRuntime(U32.user32Impl)
        val mm = rt.memoryManager
        val classNameBuffer = mm.allocate(64)
        U32.GetClassNameW(hwnd, classNameBuffer, 64)
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