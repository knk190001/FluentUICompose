package interop

import jnr.ffi.Runtime
import jnr.ffi.Struct
object WindowClassManager {
    fun init() {
        RegisterClassExHook.installHook { wndclassexw ->
            val atom = User32.RegisterClassExW(wndclassexw)
            val clsStruct = WNDCLASSEXW(Runtime.getSystemRuntime())
            clsStruct.useMemory(wndclassexw)

            val cls = clsStruct.clsName
            if (cls == "SunAwtFrame") {
                val rt = Runtime.getRuntime(User32.user32Impl)
                val mm = rt.memoryManager

                registerClasses(clsStruct)
                RegisterClassExHook.uninstallHook()
            }
            atom
        }
    }

    private fun registerClasses(referenceClass: WNDCLASSEXW) {
        val style = referenceClass.style.get() or 0x00020000
        registerClassWithStyle("EphemeralFrame",style,referenceClass)
    }

    private fun registerClassWithStyle(name: String, newStyle: Long, referenceClass: WNDCLASSEXW) {
        val rt = Runtime.getRuntime(User32.user32Impl)
        val (newClass,ptr) = referenceClass.copy {
            WNDCLASSEXW(rt)
        }

        newClass.style.set(newStyle)
        newClass.lpszClassName.set(name.toWCharArray(rt))

        val returnValue = User32.user32Impl.RegisterClassExW(ptr)
    }
}