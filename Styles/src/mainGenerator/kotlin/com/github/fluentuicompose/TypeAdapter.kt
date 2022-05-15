package com.github.fluentuicompose

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import org.json.JSONObject
import java.lang.reflect.Constructor

abstract class TypeAdapter<J,T>(val type:String) {
    abstract fun fromJSON(jsonType: J):T

    abstract fun addToType(value:T, context:GeneratorContext)

    abstract fun getInitializerCodeBlock(value:T, ):CodeBlock
}

data class GeneratorContext(val typeBuilder:TypeSpec.Builder,val  constructorBuilder: FunSpec.Builder)
