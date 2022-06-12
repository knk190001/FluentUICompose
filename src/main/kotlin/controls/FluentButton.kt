@file:Suppress("FunctionName")

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import controls.FluentTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FluentAccentButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable RowScope.(ButtonState) -> Unit
) {
    var state by remember {
        mutableStateOf(ButtonState.None)
    }
    FluentAccentButtonStateless(
        modifier
            .onPointerEvent(PointerEventType.Enter) {
                state = ButtonState.Hover
            }
            .onPointerEvent(PointerEventType.Exit) {
                state = ButtonState.None
            }
            .onPointerEvent(PointerEventType.Press) {
                state = ButtonState.Pressed
            }
            .onPointerEvent(PointerEventType.Release) {
                state = ButtonState.None
            }, onClick, state, content
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FluentButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable RowScope.(ButtonState) -> Unit
) {
    var state by remember {
        mutableStateOf(ButtonState.None)
    }
    FluentButtonStateless(
        modifier
            .onPointerEvent(PointerEventType.Enter) {
                state = ButtonState.Hover
            }
            .onPointerEvent(PointerEventType.Exit) {
                state = ButtonState.None
            }
            .onPointerEvent(PointerEventType.Press) {
                state = ButtonState.Pressed
            }
            .onPointerEvent(PointerEventType.Release) {
                state = ButtonState.None
            }, onClick, state, content
    )
}

enum class ButtonState {
    None, Hover, Pressed, Disabled
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
fun ButtonState.getAccentTextColor(): Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.textOnAccent.primary
        ButtonState.Hover -> FluentTheme.colors.fillColor.textOnAccent.primary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.textOnAccent.secondary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.textOnAccent.disabled
    }
}

@Composable
fun ButtonState.getFill():Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.control.default
        ButtonState.Hover -> FluentTheme.colors.fillColor.control.secondary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.control.tertiary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.control.disabled
    }
}


@Composable
fun ButtonState.getTextColor():Color {
    return when (this) {
        ButtonState.None -> FluentTheme.colors.fillColor.text.primary
        ButtonState.Hover -> FluentTheme.colors.fillColor.text.primary
        ButtonState.Pressed -> FluentTheme.colors.fillColor.text.secondary
        ButtonState.Disabled -> FluentTheme.colors.fillColor.text.disabled
    }
}

@Composable
fun FluentAccentButtonStateless(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: ButtonState,
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    Row(
        modifier
            .clickable(
                state != ButtonState.Disabled,
                onClick = onClick
            ).requiredWidth(100.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, FluentTheme.gradients.elevation.accentControl.border, RoundedCornerShape(4.dp))
            .background(state.getAccentFill())
            .padding(start = 12.dp, top = 5.dp, end = 12.dp, bottom = 7.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        content(state)
    }
}

@Composable
fun FluentButtonStateless(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: ButtonState,
    content: @Composable RowScope.(state: ButtonState) -> Unit
) {
    Row(
        modifier
            .clickable(
                state != ButtonState.Disabled,
                onClick = onClick
            ).requiredWidth(100.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, FluentTheme.gradients.elevation.control.border, RoundedCornerShape(4.dp))
            .background(state.getFill())
            .padding(start = 12.dp, top = 5.dp, end = 12.dp, bottom = 7.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        content(state)
    }
}
