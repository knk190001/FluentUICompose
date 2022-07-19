@file:Suppress("FunctionName")

package controls

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.github.knk190001.fluentuicompose.generated.SerializedFont

@Composable
fun FluentText(text: String, modifier: Modifier = Modifier, color: Color = LocalTextColor.current, font: SerializedFont, textDecoration: TextDecoration = TextDecoration.None) {
    Text(
        text,
        modifier,
        color,
        fontSize = font.fontSize.sp,
        textDecoration = textDecoration,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(font.fontWeight),
        fontStyle = FontStyle.Normal, //TODO: Fix this
        letterSpacing = font.letterSpacing.sp,
        lineHeight = font.lineHeight.sp,

        //TODO: Add paragraph indent, paragraph spacing, and text case`
    )
}