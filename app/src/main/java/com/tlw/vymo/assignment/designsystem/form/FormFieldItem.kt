package com.tlw.vymo.assignment.designsystem.form

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.tlw.vymo.assignment.designsystem.atoms.VymoCheckbox
import com.tlw.vymo.assignment.designsystem.atoms.VymoSelect
import com.tlw.vymo.assignment.designsystem.atoms.VymoTextField
import com.tlw.vymo.assignment.designsystem.molecules.VymoFormField

private const val TEXT_AREA_MIN_LINES = 4

/** Maps a config entry to its atom, wrapped in the field molecule. */
@Composable
internal fun FormFieldItem(
    field: FieldConfig,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onFocusLost: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isError = error != null

    VymoFormField(
        // The checkbox shows its label next to the box.
        label = field.label.takeUnless { field.type == FieldType.Checkbox },
        required = field.isRequired,
        hint = field.hint,
        error = error,
        modifier = modifier,
    ) {
        when (field.type) {
            FieldType.Text, FieldType.Email, FieldType.TextArea -> VymoTextField(
                value = value,
                onValueChange = onValueChange,
                label = field.label,
                placeholder = field.placeholder,
                isError = isError,
                singleLine = field.type != FieldType.TextArea,
                minLines = if (field.type == FieldType.TextArea) TEXT_AREA_MIN_LINES else 1,
                keyboardOptions = field.keyboardOptions(),
                onFocusLost = onFocusLost,
            )

            FieldType.Select -> VymoSelect(
                value = value,
                options = field.options,
                onValueChange = onValueChange,
                label = field.label,
                placeholder = field.placeholder,
                isError = isError,
                onFocusLost = onFocusLost,
            )

            FieldType.Checkbox -> VymoCheckbox(
                checked = value == CHECKED,
                onCheckedChange = { checked ->
                    onValueChange(if (checked) CHECKED else "")
                    onFocusLost()
                },
                label = field.label,
                isError = isError,
            )
        }
    }
}

private fun FieldConfig.keyboardOptions(): KeyboardOptions = when (type) {
    FieldType.Email -> KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
    )

    FieldType.TextArea -> KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = ImeAction.Default,
    )

    else -> KeyboardOptions(
        keyboardType = if (keyboard == FieldKeyboard.Phone) KeyboardType.Phone else KeyboardType.Text,
        capitalization = if (keyboard == FieldKeyboard.Phone) {
            KeyboardCapitalization.None
        } else {
            KeyboardCapitalization.Words
        },
        imeAction = ImeAction.Next,
    )
}
