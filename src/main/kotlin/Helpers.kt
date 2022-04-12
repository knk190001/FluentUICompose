
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import org.jetbrains.skiko.SkiaLayer
import javax.swing.JLayeredPane

fun enableTransparency(window: ComposeWindow) {
    val composeLayer = window.contentPane.getComponent(0) as JLayeredPane
    val skiaLayer = composeLayer.getComponent(0) as SkiaLayer
    composeLayer.remove(0)
    skiaLayer.transparency = true
    composeLayer.add(skiaLayer,1)
}

@Suppress("FunctionName")
fun ColorIIIF(r: Int, g: Int, b: Int, a: Float):Color {
    return Color(r,g,b,(a*255).toInt())
}