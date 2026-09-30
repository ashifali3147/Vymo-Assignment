package com.tlw.vymo.assignment.designsystem.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

/** Phone portrait: one column, every field full width. */
@Composable
internal fun CompactFormLayout(
    fields: List<FieldConfig>,
    modifier: Modifier = Modifier,
    fieldContent: @Composable (FieldConfig, Modifier) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(VymoTheme.spacing.lg),
    ) {
        fields.forEach { field ->
            key(field.name) {
                fieldContent(field, Modifier.fillMaxWidth())
            }
        }
    }
}
