package interop

import jnr.ffi.Pointer
import jnr.ffi.annotations.Delegate
import jnr.ffi.types.u_int32_t

fun interface CreateWindowExWHook {
    @Delegate
    fun invoke(
        @u_int32_t dwExStyle: Long,
        lpClassName: Pointer?,
        lpWindowName: Pointer?,
        @u_int32_t dwStyle: Long,
        x: Int,
        y: Int,
        nWidth: Int,
        nHeight: Int,
        hWndParent: Pointer?,
        hMenu: Pointer?,
        hInstance: Pointer?,
        lpParam: Pointer?
    ): Pointer?
}