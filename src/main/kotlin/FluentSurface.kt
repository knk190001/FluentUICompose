import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FluentSurface(modifier: Modifier = Modifier, elevation: Dp = 0.dp, content: @Composable () -> Unit = {}) {
    val backgroundColor = FluentTheme.colors.background.fillColor.cardBackground.default
    val strokeColor = FluentTheme.colors.strokeColor.cardStroke.default
    Box(modifier
        .shadow(elevation, RoundedCornerShape(7.dp))
        .border(1.dp, strokeColor, RoundedCornerShape(7.dp))
        .drawBehind {
            drawRoundRect(
                backgroundColor,
                cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx()),
                blendMode = BlendMode.Src
            )
        }
    ) {
        content()
    }
}