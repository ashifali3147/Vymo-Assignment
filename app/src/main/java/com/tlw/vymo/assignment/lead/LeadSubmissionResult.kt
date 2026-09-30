package com.tlw.vymo.assignment.lead

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.vymo.assignment.R
import com.tlw.vymo.assignment.designsystem.atoms.VymoButton
import com.tlw.vymo.assignment.designsystem.atoms.VymoButtonVariant
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme
import com.tlw.vymo.assignment.designsystem.tokens.VymoBreakpoints

/** Replaces the form once a lead is submitted. */
@Composable
fun LeadSubmissionResult(
    values: List<SubmittedValue>,
    modifier: Modifier = Modifier,
) {
    val colors = VymoTheme.colors
    val spacing = VymoTheme.spacing
    val shape = RoundedCornerShape(VymoTheme.shapes.cornerMedium)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.xl),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            Text(
                text = stringResource(R.string.lead_form_submitted_title),
                style = VymoTheme.typography.title,
                color = colors.onSurface,
                modifier = Modifier.semantics {
                    heading()
                    liveRegion = LiveRegionMode.Polite
                },
            )
            Text(
                text = stringResource(R.string.lead_form_submitted_subtitle),
                style = VymoTheme.typography.caption,
                color = colors.onSurfaceMuted,
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface, shape)
                .border(VymoTheme.shapes.borderThin, colors.disabled, shape)
                .padding(spacing.lg),
        ) {
            // Same breakpoint as the form, so a wide screen shows values side by side.
            val columns = if (maxWidth >= VymoBreakpoints.TwoColumnMinWidth) 2 else 1
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                values.chunked(columns).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.xl)) {
                        row.forEach { SubmittedValueItem(it, Modifier.weight(1f)) }
                        repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubmittedValueItem(item: SubmittedValue, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(VymoTheme.spacing.xs),
    ) {
        Text(
            text = item.label,
            style = VymoTheme.typography.caption,
            color = VymoTheme.colors.onSurfaceMuted,
        )
        Text(
            text = item.value,
            style = VymoTheme.typography.body,
            color = VymoTheme.colors.onSurface,
        )
    }
}

@Composable
fun LeadSubmissionActions(
    onEdit: () -> Unit,
    onNewLead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VymoTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VymoButton(
            label = stringResource(R.string.lead_form_edit),
            onClick = onEdit,
            variant = VymoButtonVariant.Secondary,
            modifier = Modifier.weight(1f),
        )
        VymoButton(
            label = stringResource(R.string.lead_form_new_lead),
            onClick = onNewLead,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LeadSubmissionResultPreview() {
    VymoTheme {
        LeadSubmissionResult(
            values = listOf(
                SubmittedValue("Full name", "Jane Doe"),
                SubmittedValue("Email", "jane@example.com"),
                SubmittedValue("Lead type", "Individual"),
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}
