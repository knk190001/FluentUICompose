package interop

import jnr.ffi.Runtime
import jnr.ffi.Struct
import jnr.ffi.util.EnumMapper

@Suppress("SpellCheckingInspection")
enum class DWMAttribute(val value:Int) : EnumMapper.IntegerEnum {
    DWMA_USE_IMMERSIVE_DARK_MODE(20),
    DWMWA_SYSTEMBACKDROP_TYPE(38),
    DWMA_MICA_EFFECT(1029);

    override fun intValue(): Int {
        return value
    }
}