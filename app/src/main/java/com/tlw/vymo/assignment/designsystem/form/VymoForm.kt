package com.tlw.vymo.assignment.designsystem.form

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tlw.vymo.assignment.designsystem.tokens.VymoBreakpoints

/**
 * Renders one field per visible config entry.
 *
 * The form is stateless: values and errors come in, changes go out through the callbacks.
 * It picks the compact or expanded layout from the width it is given.
 */
@Composable
fun VymoForm(
    fields: List<FieldConfig>,
    values: FormValues,
    errors: Map<String, String>,
    onValueChange: (name: String, value: String) -> Unit,
    onFieldFocusLost: (name: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visible = fields.visibleFields(values)

    val fieldContent: @Composable (FieldConfig, Modifier) -> Unit = { field, fieldModifier ->
        FormFieldItem(
            field = field,
            value = values.valueOf(field),
            error = errors[field.name],
            onValueChange = { onValueChange(field.name, it) },
            onFocusLost = { onFieldFocusLost(field.name) },
            modifier = fieldModifier,
        )
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val layoutModifier = Modifier.animateContentSize()
        if (maxWidth >= VymoBreakpoints.TwoColumnMinWidth) {
            ExpandedFormLayout(visible, layoutModifier, fieldContent)
        } else {
            CompactFormLayout(visible, layoutModifier, fieldContent)
        }
    }
}
