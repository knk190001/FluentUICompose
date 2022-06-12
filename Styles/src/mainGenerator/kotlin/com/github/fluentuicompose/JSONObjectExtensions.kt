package com.github.fluentuicompose

import org.json.JSONObject

internal fun JSONObject.forEach(body: (String, Any) -> Unit) {
    this.keys().forEach { body(it, this[it]) }
}

internal fun JSONObject.traverse(perNode: (String, Any, () -> Unit) -> Unit) {
    this.forEach { key, value ->
        perNode(key, value) {
            if (value !is JSONObject) throw IllegalStateException("Can't traverse non JSONObject type")
            value.traverse(perNode)
        }

    }
}

internal fun JSONObject.isCollection(): Boolean {
    return !this.keySet().contains("type")
}

private val ignored = listOf(
    "org.lukasoppermann.figmaDesignTokens",
    "extensions",
    "description"
)

internal fun JSONObject.toDesignTokenCollection(rootName: String = ""): DesignTokenCollection {
    var current = DesignTokenCollection(rootName, mutableMapOf())
    this.traverse { key, value, traverseChild ->
        if (ignored.contains(key)) return@traverse
        if (value !is JSONObject) return@traverse
        if (value.isCollection()) {
            val prev = current
            current = DesignTokenCollection(key.toPascalCase(), mutableMapOf(), prev)
            traverseChild()
            prev[key] = current
            current = prev
        } else {
            current[key] = GenericToken(key, value.getString("type"), value["value"])
        }
    }
    return current
}

