package com.tlw.vymo.assignment.designsystem.atoms

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

@Composable
fun VymoButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = VymoTheme.colors

    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = MinTouchHeight),
        enabled = enabled,
        shape = RoundedCornerShape(VymoTheme.shapes.cornerMedium),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary,
            disabledContainerColor = colors.disabled,
            disabledContentColor = colors.onDisabled,
        ),
        contentPadding = PaddingValues(
            horizontal = VymoTheme.spacing.xl,
            vertical = VymoTheme.spacing.md,
        ),
    ) {
        Text(text = label, style = VymoTheme.typography.button)
    }
}

@Preview(showBackground = true)
@Composable
private fun VymoButtonPreview() {
    VymoTheme {
        VymoButton(label = "Submit", onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
