package com.github.fluentuicompose

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

interface CodeSerializable {
    fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName?
    fun getInitializerCodeBlock(appendInitializer: Boolean = true): CodeBlock
}