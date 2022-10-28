package interop

import com.github.knk190001.easyhook_java.LocalHook
import jnr.ffi.Pointer
import jnr.ffi.annotations.Delegate

object RegisterClassExHook {
    private lateinit var hook: LocalHook

    fun installHook(newFunction: IRegisterClassExW){
        val target = LocalHook.GetProcAddress("user32","RegisterClassExW")

        hook = LocalHook.create(target,newFunction,IRegisterClassExW::class.java,null)
        hook.setExclusiveACL(LongArray(0))
    }

    fun uninstallHook(){
        hook.uninstall()
    }
}

fun interface IRegisterClassExW {
    @Delegate
    fun invoke(wndclassexw: Pointer?):Short
}