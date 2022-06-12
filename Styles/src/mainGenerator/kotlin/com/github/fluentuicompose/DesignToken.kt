package com.github.fluentuicompose

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.github.fluentuicompose.genV2.*
import com.squareup.kotlinpoet.*

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import kotlin.reflect.full.memberProperties

sealed class DesignToken(val name: String)
abstract class SpecificToken(name: String) : CodeSerializable, DesignToken(name)

val specifier = mapOf<String, (GenericToken) -> SpecificToken>(
    "color" to { token ->
        if (token.value !is String) throw IllegalArgumentException("Non string color value given")
        val value = token.value
        ColorToken(token.name.toPascalCase(), value.rgbaToArgb())
    },
    "custom-fontStyle" to { token ->
        if (token.value !is JSONObject) throw IllegalArgumentException("Non JSONOBject value given")
        FontToken(
            token.name.toPascalCase(),
            Json.decodeFromString(token.value.toString())
        )
    },
    "custom-gradient" to { token ->
        if (token.value !is JSONObject) throw IllegalArgumentException("Non JSONOBject value given")
        val gradient =  GradientToken(token.name.toPascalCase(),Json.decodeFromString(token.value.toString()))
        gradient.copy(
            value = gradient.value.copy(stops = gradient.value.stops.map {
                it.copy(color = it.color.rgbaToArgb())
            })
        )
    }
)

class GenericToken(name: String, val type: String, val value: Any) : DesignToken(name)

class DesignTokenCollection(
    name: String,
    val children: MutableMap<String, DesignToken>,
    var parent: DesignTokenCollection? = null
) : CodeSerializable,
    DesignToken(name) {

    operator fun get(key: String): DesignToken? = children[key]

    operator fun set(key: String, value: DesignToken) {
        children[key] = value
    }

    fun traverse(perNode: (String, DesignToken, () -> Unit) -> Unit) {
        children.forEach { (key, value) ->
            perNode(key, value) {
                if (value !is DesignTokenCollection) throw IllegalStateException("Can't traverse non DesignTokenCollection type")
                value.traverse(perNode)
            }
        }
    }

    fun specialize() {
        var parent = this
        traverse { name, token, traverseChild ->
            if (token is DesignTokenCollection) {
                val prevParent = parent
                parent = token
                traverseChild()
                parent = prevParent
            } else if (token is GenericToken && specifier.containsKey(token.type)) {
                parent[name] = specifier[token.type]!!(token)
            }
        }
    }

    override fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName {
        val childClassConstructor = FunSpec.constructorBuilder()
        val childClass = TypeSpec.classBuilder(name)

        childClass.addModifiers(KModifier.DATA)

        children.values.filter {
            it is CodeSerializable
        }.map {
            it as CodeSerializable
        }.mapNotNull {
            it.addToType(childClass, childClassConstructor)
        }.forEach { className ->
            childClassConstructor.addParameter(
                ParameterSpec
                    .builder(className.simpleName.toCamelCase(), className)
                    .build()
            )
            childClass.addProperty(
                PropertySpec.builder(className.simpleName.toCamelCase(), className)
                    .initializer(className.simpleName.toCamelCase())
                    .build()
            )
        }
        val typeSpec = childClass.primaryConstructor(childClassConstructor.build()).build()
        builder.addType(typeSpec)
        return ClassName("", typeSpec.name!!)
    }

    private fun getContainingClasses(): List<String> {
        val result = mutableListOf<String>()
        var current = parent
        while (current != null) {
            result.add(current.name)
            current = current.parent
        }
        return result.reversed()
    }

    override fun getInitializerCodeBlock(appendInitializer: Boolean): CodeBlock {
        val containingClasses = getContainingClasses()

        val block = CodeBlock
            .builder()
            .add(if (appendInitializer) "${name.toCamelCase().sanitizeMemberName()} = " else "")
            .add("%T(", ClassName(packageName, containingClasses.toMutableList().also { it.add(name) }))
            .add("\n")
            .indent()

        children.values.filter {
            it is CodeSerializable
        }.map {
            it as CodeSerializable
        }.map {
            it.getInitializerCodeBlock()
        }.forEach {
            block.add(it)
            block.add(",\n")
        }
        block
            .unindent()
            .add(")")
        return block.build()
    }

    fun copy(
        name: String = this.name,
        children: MutableMap<String, DesignToken> = this.children,
        parent: DesignTokenCollection? = this.parent
    ): DesignTokenCollection {
        val designTokenCollection = DesignTokenCollection(name, children, parent)
        designTokenCollection.children.forEach { (_, value) ->
            if (value is DesignTokenCollection) {
                value.parent = designTokenCollection
            }
        }
        return designTokenCollection

    }
}


class FontToken(name: String, val value: SerializedFont) : SpecificToken(name) {
    companion object {
        private var serializedFontClassName: ClassName? = null
        fun addBaseClass(): TypeSpec {
            if (serializedFontClassName != null) throw IllegalStateException("Font base class already added")

            val spec = TypeSpec.classBuilder("SerializedFont")
                .addModifiers(KModifier.DATA)

            val constructorSpec = FunSpec.constructorBuilder()


            SerializedFont::class.memberProperties.forEach {
                constructorSpec.addParameter(it.name, it.returnType.asTypeName())
                spec.addProperty(
                    PropertySpec
                        .builder(it.name, it.returnType.asTypeName())
                        .initializer(it.name)
                        .build()
                )
            }

            spec.primaryConstructor(constructorSpec.build())
            return spec.build().also {
                serializedFontClassName = ClassName(packageName, it.name!!)
            }

        }
    }

    override fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName? {
        if (serializedFontClassName == null) throw IllegalStateException("SerializedFontClassName not yet initialized")
        constructorBuilder.addParameter(name.toCamelCase(), serializedFontClassName!!)

        builder.addProperty(
            PropertySpec.builder(name.toCamelCase(), serializedFontClassName!!).initializer(name.toCamelCase()).build()
        )
        return null
    }

    override fun getInitializerCodeBlock(appendInitializer: Boolean): CodeBlock {
        val block = CodeBlock.builder()
            .add(if (appendInitializer) "${name.toCamelCase().sanitizeMemberName()} = " else "")
            .add("%T(", serializedFontClassName)
            .indent()
        SerializedFont::class.memberProperties.forEach {
            block.add("${it.name.toCamelCase()} = ")
                .add(if (it.get(value) is String) "%S" else "%L", "${it.get(value)}")
                .add(",\n")
        }
        block.unindent()
            .add(")\n")
        return block.build()
    }
}

class ColorToken(name: String, val value: String) : SpecificToken(name) {
    override fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName? {
        constructorBuilder.addParameter(
            ParameterSpec.builder(name.toCamelCase(), Color::class)
                .defaultValue("%T(0x$value)", Color::class)
                .build()
        )

        builder.addProperty(
            PropertySpec.builder(name.toCamelCase(), Color::class)
                .initializer(name.toCamelCase())
                .build()
        )
        return null
    }

    override fun getInitializerCodeBlock(appendInitializer: Boolean): CodeBlock {
        val block = CodeBlock.builder()
            .add(if (appendInitializer) "${name.toCamelCase().sanitizeMemberName()} = " else "")
            .add("%T(", Color::class)
            .add("0x")
            .add("%L", value)
            .add(")")
        return block.build()
    }
}

class GradientToken(name: String, val value: GradientInfo) : SpecificToken(name) {
    override fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName? {
        constructorBuilder.addParameter(
            ParameterSpec.builder(name.toCamelCase(), Brush::class)
                .defaultValue(
                    value.initializer
                )
                .build()
        )

        builder.addProperty(
            PropertySpec.builder(name.toCamelCase(),Brush::class)
                .initializer(name.toCamelCase())
                .build()
        )
        return null
    }

    override fun getInitializerCodeBlock(appendInitializer: Boolean): CodeBlock {
        return CodeBlock.builder()
            .add(if (appendInitializer) "${name.toCamelCase().sanitizeMemberName()} = " else "")
            .add(value.initializer)
            .build()
    }

    fun copy(name: String = this.name, value: GradientInfo = this.value):GradientToken {
        return GradientToken(name,value)
    }
}

