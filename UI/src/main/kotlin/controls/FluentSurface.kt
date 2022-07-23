import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import controls.FluentTheme
import controls.StartColorStack

@Composable
fun FluentSurface(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    solid: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    val backgroundColor =
        if (solid) FluentTheme.colors.background.fillColor.solidBackground.base else FluentTheme.colors.background.fillColor.cardBackground.default
    //val backgroundColor = Color(0xFF1c1c1c)
    val strokeColor = FluentTheme.colors.strokeColor.cardStroke.default

    StartColorStack(backgroundColor) {
        Box(modifier
            .shadow(elevation, RoundedCornerShape(7.dp))
            .drawBehind {
                drawRoundRect(
                    backgroundColor,
                    cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx()),
                    blendMode = BlendMode.Src
                )
                drawRoundRect(
                    strokeColor,
                    cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx()),
                    style = Stroke(1.dp.toPx()),
                    blendMode = BlendMode.Src
                )
            }
        ) {
            content()
        }
    }

}

@Composable
fun ShadowedBox(
    modifier: Modifier = Modifier,
    brush: Brush,
    strokeBrush: Brush,
    radius: Dp,
    elevation: Dp = 0.dp,
    contentAlignment: Alignment,
    content: @Composable BoxScope.() -> Unit = {}
) {
//    Box(
//        modifier
//            .shadow(elevation, RoundedCornerShape(radius))
//            .background(brush, RoundedCornerShape(radius))
//            .border(1.dp,strokeBrush, RoundedCornerShape(radius)),
//        contentAlignment,
//    ){
//        content()
//    }

    Box(
        modifier
            .shadow(elevation, RoundedCornerShape(radius))
            .drawBehind {
                this.density
                drawRoundRect(
                    brush,
                    cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                    blendMode = BlendMode.Src
                )
                drawRoundRect(
                    strokeBrush,
                    cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                    style = Stroke(1.dp.toPx()),
                    //blendMode = BlendMode.Src
                )
            },
        contentAlignment = contentAlignment
    ) {
        content()
    }
}