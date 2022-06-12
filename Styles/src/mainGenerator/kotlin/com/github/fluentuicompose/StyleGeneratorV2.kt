package com.github.fluentuicompose

import com.github.fluentuicompose.genV2.*
import com.squareup.kotlinpoet.*
import io.heartpattern.gcg.api.Generator
import io.heartpattern.gcg.api.kotlin.KotlinCodeGenerator
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File

@Generator
class StyleGeneratorV2 : KotlinCodeGenerator {
    override fun generateKotlin(): Collection<FileSpec> {


        val fileSpec = FileSpec.builder(packageName, fileName)
        val unused1 = TypeSpec.classBuilder("Colors")
        val unusedConstructor1 = FunSpec.constructorBuilder()
        val tokensRaw = JSONTokener(String(File("design-tokens.tokens.json").readBytes())).nextValue() as JSONObject
        val tokens = tokensRaw.toDesignTokenCollection("Colors")
        tokens.specialize()

        fileSpec.addType(FontToken.addBaseClass())


        val colorTokens = tokens["color"] as DesignTokenCollection
        val lightDesignTokens = (colorTokens["light"] as DesignTokenCollection).copy(
            name = "FluentColors",
            parent = null
        )

        val darkDesignTokens = (colorTokens["dark"] as DesignTokenCollection).copy(
            name = "FluentColors",
            parent = null
        )

        lightDesignTokens.addToType(unused1, unusedConstructor1)
        unused1.primaryConstructor(unusedConstructor1.build())
        fileSpec.addType(unused1.build().typeSpecs[0])

        val fontTokens = (tokens["font"] as DesignTokenCollection).copy(
            name = "FluentFonts",
            parent = null
        )


        val unused2 = TypeSpec.classBuilder("Unused")
        val unusedConstructor2 = FunSpec.constructorBuilder()

        fontTokens.addToType(unused2, unusedConstructor2)
        unused2.primaryConstructor(unusedConstructor2.build())
        fileSpec.addType(unused2.build().typeSpecs[0])

        fileSpec.addProperty(
            PropertySpec.builder("fonts", ClassName(packageName, fontTokens.name))
                .initializer(fontTokens.getInitializerCodeBlock(false))
                .build()
        )

        fileSpec.addProperty(
            PropertySpec.builder("light", ClassName(packageName, lightDesignTokens.name))
                .initializer(lightDesignTokens.getInitializerCodeBlock(false))
                .build()
        )

        fileSpec.addProperty(
            PropertySpec.builder("dark", ClassName(packageName, darkDesignTokens.name))
                .initializer(darkDesignTokens.getInitializerCodeBlock(false))
                .build()
        )

        val unused3 = TypeSpec.classBuilder("Unused")
        val unusedConstructor3 = FunSpec.constructorBuilder()

        val gradientTokens = tokens["gradient"] as DesignTokenCollection
        val lightGradientTokens = (gradientTokens["light"] as DesignTokenCollection).copy(
            name = "FluentGradients",
            parent = null
        )

        val darkGradientTokens = (gradientTokens["dark"] as DesignTokenCollection).copy(
            name = "FluentGradients",
            parent = null
        )

        lightGradientTokens.addToType(unused3, unusedConstructor2)
        unused3.primaryConstructor(unusedConstructor3.build())
        fileSpec.addType(unused3.build().typeSpecs[0])

        fileSpec.addProperty(
            PropertySpec.builder("lightGradient",ClassName(packageName,lightGradientTokens.name))
                .initializer(lightGradientTokens.getInitializerCodeBlock(false))
                .build()
        )

        fileSpec.addProperty(
            PropertySpec.builder("darkGradient", ClassName(packageName,darkGradientTokens.name))
                .initializer(darkGradientTokens.getInitializerCodeBlock(false))
                .build()
        )

        return listOf(fileSpec.build())
    }
}