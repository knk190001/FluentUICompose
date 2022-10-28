package interop

import jnr.ffi.Pointer
import jnr.ffi.Struct

fun String.toWCharArray(rt: jnr.ffi.Runtime): Pointer {
    val mm = rt.memoryManager
    val buffer = mm.allocate(2 * (this.count() + 1))

    for (index in this.indices) {
        buffer.putShort(index * 2L, this[index].code.toShort())
    }
    buffer.putShort(2L * this.count(), 0)

    return buffer
}

fun Pointer.wCharArrayToString(): String {
    val ptr = this
    val length = this.getWCharLength()

    return buildString {
        for (i in 0 until length) {
            append(ptr.getShort(i * 2L).toInt().toChar())
        }
    }
}

fun Pointer.getWCharLength(): Int {
    var length = 0
    var wChar: Short
    do {
        wChar = this.getShort(length * 2L)
        if (wChar != (0).toShort()) {
            length++
        }
    } while (wChar != (0).toShort())
    return length
}

fun <T : Struct> T.copy(generator: () -> T): Pair<T,Pointer> {
    val size = Struct.size(this)
    val rt = runtime
    val mm = rt.memoryManager
    val newStructPtr = mm.allocate(size)
    val newStruct = generator()
    newStructPtr.transferFrom(0, Struct.getMemory(this), 0, size.toLong())
    newStruct.useMemory(newStructPtr)
    return Pair(newStruct,newStructPtr)
}
