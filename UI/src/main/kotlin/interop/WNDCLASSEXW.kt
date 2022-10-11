package interop

import jnr.ffi.Runtime
import jnr.ffi.Struct

class WNDCLASSEXW(runtime: Runtime?) : Struct(runtime) {
       val cbSize: Struct.Unsigned32 = Unsigned32()
       val style: Struct.Unsigned32 = Unsigned32()
       val lpfnWndProc: Struct.Pointer = Pointer()
       val cbClsExtra: Struct.Signed32 = Signed32()
       val cbWndExtra: Struct.Signed32 = Signed32()
       val hInstance: Struct.Pointer = Pointer()
       val hIcon: Struct.Pointer = Pointer()
       val hCursor: Struct.Pointer = Pointer()
       val hbrBackground: Struct.Pointer = Pointer()
       val lpszMenuName: Struct.Pointer = Pointer()
       val lpszClassName: Struct.Pointer = Pointer()
       val hIconSm: Struct.Pointer = Pointer()
}