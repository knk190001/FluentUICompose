package com.github.fluentuicompose

import androidx.compose.ui.graphics.Color

import com.squareup.kotlinpoet.*
import io.heartpattern.gcg.api.Generator
import io.heartpattern.gcg.api.kotlin.KotlinCodeGenerator
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File
import java.util.*
import javax.annotation.Generated
import kotlin.reflect.KClass


const val packageName = "com.github.knk190001.fluentuicompose"
const val className = "FluentTheme"
@Generator
class StyleGenerator : KotlinCodeGenerator {
    override fun generateKotlin(): Collection<FileSpec> {

        val style = JSONTokener(String(File("design-tokens.tokens.json").readBytes())).nextValue() as JSONObject
        val light = ((style["color"] as JSONObject)["light"] as JSONObject).treeForObject("FluentTheme", mutableListOf())
        val dark = ((style["color"] as JSONObject)["dark"] as JSONObject).treeForObject("FluentTheme", mutableListOf())!!

        val themeTypeSpec = TypeSpec.classBuilder("unused")
        val themeTypeConstructor = FunSpec.constructorBuilder()

        light!!.addToType(themeTypeSpec, themeTypeConstructor)

        themeTypeSpec.primaryConstructor(themeTypeConstructor.build())

        val typeSpec = themeTypeSpec.build()


        return listOf(
            FileSpec.builder(packageName, className)
                .addAnnotation(
                    AnnotationSpec.builder(Suppress::class)
                        .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                        .addMember("%S,%S", "RedundantVisibilityModifier", "unused")
                        .build()
                )
                .addAnnotation(
                    AnnotationSpec.builder(Generated::class)
                        .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                        .addMember(
                            "value = [%S], date = %S",
                            this::class.qualifiedName!!,
                            Date(System.currentTimeMillis()).toString()
                        )
                        .build()
                )
                .addType(
                    typeSpec.typeSpecs[0]
                )
                .addProperty(
                    PropertySpec
                        .builder("light", ClassName(packageName, className))
                        .initializer((light.getInitializerCodeBlock(false)))
                        .build()
                )
                .addProperty(
                    PropertySpec
                        .builder("dark", ClassName(packageName, className))
                        .initializer(dark.getInitializerCodeBlock(false))
                        .build()
                )
                .build()
        )
    }

}

abstract class Tree(val name: String, val containingClasses: List<String>) {
    abstract fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName?
    abstract fun getInitializerCodeBlock(appendInitializer:Boolean = true): CodeBlock
}

class Branch(name: String, private val children: List<Tree>, containingClasses: List<String>) : Tree(name, containingClasses) {
    override fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName {
        val childClassConstructor = FunSpec.constructorBuilder()
        val childClass = TypeSpec.classBuilder(name)

        childClass.addModifiers(KModifier.DATA)

        children.mapNotNull {
            it.addToType(childClass, childClassConstructor)
        }.forEach { className ->
            childClassConstructor.addParameter(ParameterSpec
                .builder(className.simpleName.toCamelCase(),className)
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

    override fun getInitializerCodeBlock(appendInitializer:Boolean ): CodeBlock {
        val block = CodeBlock
            .builder()
            .add(if(appendInitializer) "${name.toCamelCase().sanitizeMemberName()} = " else "")
            .add("%T(", ClassName(packageName, this.containingClasses.toMutableList().also { it.add(name) }))
            .add("\n")
            .indent()

        children.map {
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
}

class Leaf<T : Any>(name: String, private val type: KClass<T>, private val value: String, containingClasses: List<String>) :
    Tree(name, containingClasses) {

    override fun addToType(builder: TypeSpec.Builder, constructorBuilder: FunSpec.Builder): ClassName? {
//        constructorBuilder.addParameter(name, type)
        constructorBuilder.addParameter(ParameterSpec
            .builder(name,Color::class)
            .defaultValue("%T(0x${value.substring(value.length-2)+value.substring(1,value.length-2)})",Color::class)
            .build()
        )

        builder.addProperty(
            PropertySpec
                .builder(name, type)
                .initializer(name)
                .build()
        )
        return null
    }

    override fun getInitializerCodeBlock(appendInitializer: Boolean): CodeBlock {
        val block = CodeBlock
            .builder()
            .add(if(appendInitializer)"${name.sanitizeMemberName()} = " else "")
            .add("%T(", Color::class)
            .add("0x")
            .add("%L", value.substring(value.length-2)+value.substring(1,value.length-2))
            .add(")")
        return block.build()
    }
}

private fun String.sanitizeMemberName(): String {
    if (this.matches(Regex("""^[0-9]+$"""))) {
        return "`$this`"
    }
    return this
}

private fun JSONObject.isBranch(): Boolean {
    return !this.has("type")
}


private val ignored = listOf(
    "org.lukasoppermann.figmaDesignTokens",
    "extensions",
    "description"
)

private fun String.toPascalCase(): String {
    return this
        .replace(Regex("""\s([a-z])""")) {
            it.groups[0]?.value?.toUpperCase() ?: ""
        }.replace(Regex("""^([a-z])""")) {
            it.groups[0]?.value?.toUpperCase() ?: ""
        }.replace(" ", "")

}

private fun String.toCamelCase(): String {
    return this.replace(Regex("""^([A-Z])""")) {
        it.groups[0]?.value?.toLowerCase() ?: ""
    }
}

private fun JSONObject.treeForObject(name: String, nameHierarchy: MutableList<String>): Tree? {
    val newName = name.toPascalCase()

    if (ignored.contains(name)) return null
    if (isBranch()) {
        val children = this.keySet().filter {
            !ignored.contains(it)
        }.mapNotNull { key ->
            (this[key] as JSONObject).treeForObject(key, nameHierarchy.toMutableList().also {
                it.add(newName)
            })
        }.toList()
        return Branch(newName, children, nameHierarchy.toList())
    }
    val type = this["type"]
    return Leaf(
        newName.toCamelCase(), when (type) {
            "color" -> Color::class
            else -> Unit::class
        },
        this["value"].toString(),
        nameHierarchy.toMutableList()
    )
}