package com.tlw.vymo.assignment.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.vymo.assignment.designsystem.atoms.VymoTextField
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

/**
 * Wraps a control with its label, hint and error.
 *
 * [label] can be null for controls that render their own label, like a checkbox.
 * While there is an error it takes the hint's place.
 */
@Composable
fun VymoFormField(
    label: String?,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    hint: String? = null,
    error: String? = null,
    control: @Composable () -> Unit,
) {
    val colors = VymoTheme.colors
    val typography = VymoTheme.typography

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(VymoTheme.spacing.xs),
    ) {
        if (label != null) {
            Text(
                text = buildAnnotatedString {
                    append(label)
                    if (required) {
                        withStyle(SpanStyle(color = colors.error)) { append(" *") }
                    }
                },
                style = typography.label,
                color = colors.onSurface,
                // The control already announces its label.
                modifier = Modifier.clearAndSetSemantics {},
            )
        }

        control()

        when {
            error != null -> Text(
                text = error,
                style = typography.caption,
                color = colors.error,
                modifier = Modifier.semantics {
                    error(error)
                    liveRegion = LiveRegionMode.Polite
                },
            )

            hint != null -> Text(
                text = hint,
                style = typography.caption,
                color = colors.onSurfaceMuted,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VymoFormFieldPreview() {
    VymoTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            VymoFormField(label = "Name", required = true, hint = "As on your ID") {
                VymoTextField(value = "", onValueChange = {}, label = "Name")
            }
            VymoFormField(label = "Email", required = true, error = "Enter a valid email") {
                VymoTextField(value = "rahul@", onValueChange = {}, label = "Email", isError = true)
            }
        }
    }
}
