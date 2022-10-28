package interop

import jnr.ffi.Runtime
import jnr.ffi.Struct

class WNDCLASSEXW(runtime: Runtime?) : Struct(runtime) {
    var cbSize: Struct.Unsigned32 = Unsigned32()
    var style: Struct.Unsigned32 = Unsigned32()
    var lpfnWndProc: Struct.Pointer = Pointer()
    var cbClsExtra: Struct.Signed32 = Signed32()
    var cbWndExtra: Struct.Signed32 = Signed32()
    var hInstance: Struct.Pointer = Pointer()
    var hIcon: Struct.Pointer = Pointer()
    var hCursor: Struct.Pointer = Pointer()
    var hbrBackground: Struct.Pointer = Pointer()
    var lpszMenuName: Struct.Pointer = Pointer()
    var lpszClassName: Struct.Pointer = Pointer()
    var hIconSm: Struct.Pointer = Pointer()

    val clsName
           get() = lpszClassName.get().wCharArrayToString()
}