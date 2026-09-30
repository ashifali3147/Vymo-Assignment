package com.tlw.vymo.assignment.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

@Composable
fun VymoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onFocusLost: () -> Unit = {},
) {
    val colors = VymoTheme.colors
    val shapes = VymoTheme.shapes
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    var hadFocus by remember { mutableStateOf(false) }

    val borderColor = when {
        isError -> colors.error
        isFocused -> colors.borderFocused
        else -> colors.border
    }
    val borderWidth = if (isFocused || isError) shapes.borderThick else shapes.borderThin
    val shape = RoundedCornerShape(shapes.cornerMedium)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { state ->
                if (hadFocus && !state.isFocused) onFocusLost()
                hadFocus = state.isFocused
            }
            .semantics { contentDescription = label },
        enabled = enabled,
        textStyle = VymoTheme.typography.body.copy(
            color = if (enabled) colors.onSurface else colors.onDisabled,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        minLines = minLines,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(colors.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = MinTouchHeight)
                    .background(if (enabled) colors.surface else colors.disabled, shape)
                    .border(borderWidth, borderColor, shape)
                    .padding(
                        horizontal = VymoTheme.spacing.md,
                        vertical = VymoTheme.spacing.md,
                    ),
            ) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        style = VymoTheme.typography.body,
                        color = colors.onSurfaceMuted,
                    )
                }
                innerTextField()
            }
        },
    )
}

internal val MinTouchHeight = 48.dp

@Preview(showBackground = true)
@Composable
private fun VymoTextFieldPreview() {
    VymoTheme {
        VymoTextField(
            value = "",
            onValueChange = {},
            label = "Name",
            placeholder = "Jane Doe",
            modifier = Modifier.padding(16.dp),
        )
    }
}
