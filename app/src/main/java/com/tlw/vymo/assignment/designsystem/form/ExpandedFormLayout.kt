package com.tlw.vymo.assignment.designsystem.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

private const val COLUMNS = 2

/**
 * Tablet or wide landscape: two columns. Half-width fields share a row,
 * full-width fields span it. See [packRows].
 */
@Composable
internal fun ExpandedFormLayout(
    fields: List<FieldConfig>,
    modifier: Modifier = Modifier,
    fieldContent: @Composable (FieldConfig, Modifier) -> Unit,
) {
    val spacing = VymoTheme.spacing

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        packRows(fields, COLUMNS).forEach { row ->
            key(row.first().name) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.xl),
                    // Top aligned so an error under one field doesn't shift its neighbour.
                    verticalAlignment = Alignment.Top,
                ) {
                    row.forEach { field ->
                        key(field.name) {
                            fieldContent(field, Modifier.weight(1f))
                        }
                    }
                    val isLoneHalf = row.size == 1 && row.first().width == FieldWidth.Half
                    if (isLoneHalf) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
