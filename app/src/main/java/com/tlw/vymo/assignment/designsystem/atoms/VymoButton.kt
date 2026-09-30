package com.tlw.vymo.assignment.designsystem.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

enum class VymoButtonVariant { Primary, Secondary }

@Composable
fun VymoButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: VymoButtonVariant = VymoButtonVariant.Primary,
    enabled: Boolean = true,
) {
    val colors = VymoTheme.colors
    val shape = RoundedCornerShape(VymoTheme.shapes.cornerMedium)
    val contentPadding = PaddingValues(
        horizontal = VymoTheme.spacing.xl,
        vertical = VymoTheme.spacing.md,
    )
    val sizedModifier = modifier.defaultMinSize(minHeight = MinTouchHeight)
    val content: @Composable () -> Unit = {
        Text(text = label, style = VymoTheme.typography.button)
    }

    when (variant) {
        VymoButtonVariant.Primary -> Button(
            onClick = onClick,
            modifier = sizedModifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
                disabledContainerColor = colors.disabled,
                disabledContentColor = colors.onDisabled,
            ),
            contentPadding = contentPadding,
        ) { content() }

        VymoButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick,
            modifier = sizedModifier,
            enabled = enabled,
            shape = shape,
            border = BorderStroke(
                VymoTheme.shapes.borderThin,
                if (enabled) colors.primary else colors.disabled,
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colors.surface,
                contentColor = colors.primary,
                disabledContentColor = colors.onDisabled,
            ),
            contentPadding = contentPadding,
        ) { content() }
    }
}

@Preview(showBackground = true)
@Composable
private fun VymoButtonPreview() {
    VymoTheme {
        VymoButton(label = "Submit", onClick = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun VymoSecondaryButtonPreview() {
    VymoTheme {
        VymoButton(
            label = "Cancel",
            onClick = {},
            variant = VymoButtonVariant.Secondary,
            modifier = Modifier.padding(16.dp),
        )
    }
}
