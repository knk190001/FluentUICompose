@file:Suppress("FunctionName")

package controls

import ShadowedBox
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.github.knk190001.fluentuicompose.generated.fonts

@Composable
fun FluentAccentButton(
    modifier: Modifier = Modifier, onClick: () -> Unit = {}, content: @Composable RowScope.(ButtonState) -> Unit
) {
    var state by remember {
        mutableStateOf(ButtonState.None)
    }
    SetLocalTextColor(state.getOnAccentTextColor()){
        FluentAccentButtonStateless(modifier.setButtonState { state = it }, onClick, state, content)
    }
}

@Composable
fun FluentButton(
    modifier: Modifier = Modifier, onClick: () -> Unit = {}, content: @Composable RowScope.(ButtonState) -> Unit
) {
    var state by remember {
        mutableStateOf(ButtonState.None)
    }
    SetLocalTextColor(state.getTextColor()){
        FluentButtonStateless(modifier.setButtonState { state = it }, onClick, state, content)
    }
}

@Composable
fun FluentToggleButton(
    modifier: Modifier = Modifier,
    onToggle: (Boolean) -> Unit = {},
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    var state by remember {
        mutableStateOf(ButtonState.None)
    }

    var toggleState by remember {
        mutableStateOf(false)
    }

    val primary = if (toggleState) state.getAccentFill() else state.getFill()
    val stroke = FluentTheme.gradients.elevation.control.border

    SetLocalTextColor(if (toggleState) state.getOnAccentTextColor() else state.getTextColor()) {
        FluentButtonGeneric(modifier.setButtonState { state = it }, {
            toggleState = !toggleState
            onToggle(toggleState)
        }, state, primary, stroke, true, content)
    }
}

@Composable
fun FluentHyperlinkButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    var state by remember {
        mutableStateOf(ButtonState.None)
    }

    FluentHyperlinkButtonStateless(modifier.setButtonState { state = it }, onClick, state, content)
}

@Composable
fun FluentHyperlink(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: String
){
    var state by remember {
        mutableStateOf(ButtonState.None)
    }

    FluentText(
        content,
        modifier
            .clickable(onClick = onClick)
            .setButtonState { state = it },
        font = fonts.body,
        textDecoration = if (state == ButtonState.Hover || state == ButtonState.Pressed) TextDecoration.None else TextDecoration.Underline,
        color = state.getAccentTextColor()
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Modifier.setButtonState(setState: (ButtonState) -> Unit): Modifier {
    var hovering by remember {
        mutableStateOf(false)
    }

    return this.onPointerEvent(PointerEventType.Enter) {
        hovering = true
        setState(ButtonState.Hover)
    }.onPointerEvent(PointerEventType.Exit) {
        hovering = false
        setState(ButtonState.None)
    }.onPointerEvent(PointerEventType.Press) {
        setState(ButtonState.Pressed)
    }.onPointerEvent(PointerEventType.Release) {
        setState(if(hovering)ButtonState.Hover else ButtonState.None)
    }
}

@Composable
fun ButtonState.getAccentFill(): Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.accent.default
        ButtonState.Hover -> FluentTheme.colors.fillColor.accent.secondary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.accent.tertiary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.accent.disabled
    }
}

@Composable
fun ButtonState.getOnAccentTextColor(): Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.textOnAccent.primary
        ButtonState.Hover -> FluentTheme.colors.fillColor.textOnAccent.primary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.textOnAccent.secondary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.textOnAccent.disabled
    }
}

@Composable
fun ButtonState.getAccentTextColor(): Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.accentText.primary
        ButtonState.Hover -> FluentTheme.colors.fillColor.accentText.secondary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.accentText.tertiary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.accentText.disabled
    }
}

@Composable
fun ButtonState.getFill(): Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.control.default
        ButtonState.Hover -> FluentTheme.colors.fillColor.control.secondary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.control.tertiary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.control.disabled
    }
}

@Composable
fun ButtonState.getSubtleFill(): Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.control.transparent
        ButtonState.Hover -> FluentTheme.colors.fillColor.control.secondary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.control.tertiary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.control.disabled
    }
}

@Composable
fun ButtonState.getTextColor(): Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.text.primary
        ButtonState.Hover -> FluentTheme.colors.fillColor.text.primary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.text.secondary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.text.disabled
    }
}


@Composable
fun FluentHyperlinkButtonStateless(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: ButtonState,
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    val color = state.getSubtleFill()
    val brush = SolidColor(Color.Transparent)
    SetLocalTextColor(state.getAccentTextColor()) {
        FluentButtonGeneric(modifier, onClick, state, color, brush, false, content)
    }
}

@Composable
fun FluentAccentButtonStateless(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: ButtonState,
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    val color = state.getAccentFill()
    val brush = FluentTheme.gradients.elevation.control.border

    FluentButtonGeneric(modifier, onClick, state, color, brush, true, content)
}

@Composable
fun FluentButtonStateless(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: ButtonState,
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    val color = state.getFill()
    val brush = FluentTheme.gradients.elevation.control.border

    FluentButtonGeneric(modifier, onClick, state, color, brush, true, content)
}

@Composable
private fun FluentButtonGeneric(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: ButtonState,
    primary: Color,
    stroke: Brush,
    shadowEnabled: Boolean = true,
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    AddColorToStack(primary) {
        ShadowedBox(
            modifier.clickable(
                state != ButtonState.Disabled, onClick = onClick
            ).padding(start = 12.dp, top = 5.dp, end = 12.dp, bottom = 7.dp),
            SolidColor(LocalColorStack.current),
            stroke,
            4.dp,
            if (shadowEnabled) 4.dp else 0.dp,
            Alignment.Center
        ) {
            Row(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterVertically) {
                content(state)
            }
        }
    }

}
