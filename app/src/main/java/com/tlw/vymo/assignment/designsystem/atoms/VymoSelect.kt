package com.tlw.vymo.assignment.designsystem.atoms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

@Immutable
data class VymoSelectOption(
    val value: String,
    val label: String,
)

@Composable
fun VymoSelect(
    value: String,
    options: List<VymoSelectOption>,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFocusLost: () -> Unit = {},
) {
    val colors = VymoTheme.colors
    val shapes = VymoTheme.shapes
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var anchorWidthPx by remember { mutableStateOf(0) }

    val selected = options.firstOrNull { it.value == value }
    val shape = RoundedCornerShape(shapes.cornerMedium)
    val borderColor = when {
        isError -> colors.error
        expanded -> colors.borderFocused
        else -> colors.border
    }
    val borderWidth = if (expanded || isError) shapes.borderThick else shapes.borderThin

    fun dismiss() {
        expanded = false
        onFocusLost()
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = MinTouchHeight)
                .onSizeChanged { anchorWidthPx = it.width }
                .background(if (enabled) colors.surface else colors.disabled, shape)
                .border(borderWidth, borderColor, shape)
                .clip(shape)
                .clickable(enabled = enabled, role = Role.DropdownList) {
                    // Opening the menu takes focus away from whichever text field had it.
                    focusManager.clearFocus()
                    expanded = true
                }
                .semantics {
                    contentDescription = label
                    stateDescription = selected?.label ?: placeholder.orEmpty()
                }
                .padding(horizontal = VymoTheme.spacing.md, vertical = VymoTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(VymoTheme.spacing.sm),
        ) {
            Text(
                text = selected?.label ?: placeholder.orEmpty(),
                style = VymoTheme.typography.body,
                color = when {
                    !enabled -> colors.onDisabled
                    selected == null -> colors.onSurfaceMuted
                    else -> colors.onSurface
                },
                modifier = Modifier.weight(1f),
            )
            DropdownArrow(
                color = colors.onSurfaceMuted,
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = ::dismiss,
            containerColor = colors.surface,
            modifier = Modifier.width(with(density) { anchorWidthPx.toDp() }),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option.label, style = VymoTheme.typography.body)
                    },
                    onClick = {
                        onValueChange(option.value)
                        dismiss()
                    },
                )
            }
        }
    }
}

@Composable
private fun DropdownArrow(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(12.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.3f)
            lineTo(size.width, size.height * 0.3f)
            lineTo(size.width / 2f, size.height * 0.8f)
            close()
        }
        drawPath(path, color)
    }
}

@Preview(showBackground = true)
@Composable
private fun VymoSelectPreview() {
    VymoTheme {
        VymoSelect(
            value = "",
            options = listOf(
                VymoSelectOption("a", "Option A"),
                VymoSelectOption("b", "Option B"),
            ),
            onValueChange = {},
            label = "Choice",
            placeholder = "Select one",
            modifier = Modifier.padding(16.dp),
        )
    }
}
