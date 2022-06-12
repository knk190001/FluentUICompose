package com.github.fluentuicompose

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.squareup.kotlinpoet.CodeBlock

@kotlinx.serialization.Serializable
data class GradientInfo(
    val gradientType:String,
    val rotation:Int,
    val stops: List<GradientStop>
){
    val initializer:CodeBlock
        get() {
            val cb = CodeBlock.builder()
                .add("%T.verticalGradient(\n", Brush::class)
            cb.indent()
            stops.sortedBy {
                it.position
            }.forEach {
                cb.add("${it.position}f to %T(0x${it.color}),\n", Color::class)
            }
            cb.unindent()
            cb.add(")\n")

            return cb.build()
        }
}

@kotlinx.serialization.Serializable
data class GradientStop(val position:Float, val color:String)


