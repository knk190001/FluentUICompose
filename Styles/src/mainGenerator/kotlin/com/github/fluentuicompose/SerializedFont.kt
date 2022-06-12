package com.github.fluentuicompose

@kotlinx.serialization.Serializable
data class SerializedFont(
    val fontSize: Int,
    val textDecoration: String,
    val fontFamily: String,
    val fontWeight: Int,
    val fontStyle: String,
    val fontStretch: String,
    val letterSpacing: Int,
    val lineHeight: Int,
    val paragraphIndent: Int,
    val paragraphSpacing: Int,
    val textCase: String
)