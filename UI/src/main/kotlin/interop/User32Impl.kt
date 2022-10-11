package interop

import jnr.ffi.LibraryLoader
import jnr.ffi.Pointer
import jnr.ffi.types.u_int32_t

object User32{
    val user32Impl:User32Impl = LibraryLoader.create(User32Impl::class.java).load("user32")
    fun CreateWindowExW(
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
    ): Pointer? {
        val hwnd = user32Impl.CreateWindowExW(
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
        return hwnd
    }

    fun GetClassNameW(hwnd: Pointer?, lpClassName: Pointer?, nMaxCount: Int): Int {
        return user32Impl.GetClassNameW(hwnd, lpClassName, nMaxCount)
    }

    fun SetParent(hWndChild: Pointer?, hWndNewParent: Pointer?): Pointer? {
        return user32Impl.SetParent(hWndChild, hWndNewParent)
    }
}

interface User32Impl {
    fun CreateWindowExW(
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

    fun GetClassNameW(
        hwnd: Pointer?,
        lpClassName: Pointer?,
        nMaxCount: Int
    ): Int

    fun SetParent(
        hWndChild: Pointer?,
        hWndNewParent: Pointer?
    ): Pointer?


}