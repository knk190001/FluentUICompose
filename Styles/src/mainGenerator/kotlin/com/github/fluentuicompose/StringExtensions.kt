package com.github.fluentuicompose

import java.util.*

internal fun String.toPascalCase(): String {
    return this
        .replace(Regex("""\s([a-z])""")) {
            it.groups[0]?.value?.uppercase(Locale.getDefault()) ?: ""
        }.replace(Regex("""^([a-z])""")) {
            it.groups[0]?.value?.uppercase(Locale.getDefault()) ?: ""
        }.replace(" ", "")

}

internal fun String.sanitizeMemberName(): String {
    if (this.matches(Regex("""^[0-9]+$"""))) {
        return "`$this`"
    }
    return this
}

internal fun String.toCamelCase(): String {
    return this.toPascalCase().replace(Regex("""^([A-Z])""")) {
        it.groups[0]?.value?.lowercase(Locale.getDefault()) ?: ""
    }
}
internal fun String.rgbaToArgb():String{
    return this.substring(this.length - 2) + this.substring(1, this.length - 2)
}