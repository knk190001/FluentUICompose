import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import controls.FluentTheme
import org.jetbrains.skia.PathFillMode
import org.jetbrains.skia.Point3
import org.jetbrains.skia.ShadowUtils
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Composable
fun FluentSurface(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    solid: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    val backgroundColor =
        if (solid) FluentTheme.colors.background.fillColor.solidBackground.base
        else FluentTheme.colors.background.fillColor.cardBackground.default

    val strokeColor = FluentTheme.colors.strokeColor.cardStroke.default

    ShadowedBox(modifier, backgroundColor, SolidColor(strokeColor), 7.dp, 2.dp, Alignment.TopStart) {
        content()
    }

}

@Composable
fun ShadowedBox(
    modifier: Modifier = Modifier,
    color: Color,
    strokeBrush: Brush,
    radius: Dp,
    elevation: Dp = 0.dp,
    contentAlignment: Alignment,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier
            .drawWithCache {
                onDrawBehind {
                    val size = size
                    val path = Path().apply {
                        addRoundRect(RoundRect(Rect(Offset.Companion.Zero, size), CornerRadius(radius.toPx())))
                    }

                    val zParams = Point3(0f, 0f, elevation.toPx())

                    val lightPos = Point3(0f, -300.dp.toPx(), 600.dp.toPx())
                    val lightRad = 800.dp.toPx()

                    val alpha = 1f-.001f
                    //val alpha = color.alpha
                    val ambientAlpha = 0.039f * alpha
                    val spotAlpha = 0.19f * alpha
                    val ambientColor = Color.Black.copy(ambientAlpha)
                    val spotColor = Color.Black.copy(spotAlpha)

                    drawContext.canvas.withSaveLayer(size.toRect().inflate(10.dp.toPx()),Paint()){
                        ShadowUtils.drawShadow(
                            drawContext.canvas.nativeCanvas,
                            path.asSkiaPath(),
                            zParams,
                            lightPos,
                            lightRad,
                            ambientColor.toArgb(),
                            spotColor.toArgb(),
                            transparentOccluder = true,
                            geometricOnly = false
                        )

                        drawRoundRect(
                            Color.Unspecified,
                            cornerRadius = CornerRadius(radius.toPx()),
                            blendMode = BlendMode.Clear
                        )
                        drawRoundRect(
                            strokeBrush,
                            cornerRadius = CornerRadius(radius.toPx()),
                            blendMode = BlendMode.Src,
                            style = Stroke(1.5.dp.toPx())
                        )
                        drawRoundRect(
                            color,
                            cornerRadius = CornerRadius(radius.toPx()),
                            blendMode = BlendMode.DstAtop
                        )
                    }

                }
            },
//            .padding(Dp.Hairline),
            //.background(color, RoundedCornerShape(radius)),
            //.border(1.dp, strokeBrush, RoundedCornerShape(radius)),
        contentAlignment
    ) {
        content()
    }
}