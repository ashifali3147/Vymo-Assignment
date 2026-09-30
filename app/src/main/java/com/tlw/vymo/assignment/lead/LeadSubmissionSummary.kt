package com.tlw.vymo.assignment.lead

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.tlw.vymo.assignment.R
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme

@Composable
fun LeadSubmissionSummary(
    values: List<SubmittedValue>,
    modifier: Modifier = Modifier,
) {
    val colors = VymoTheme.colors
    val spacing = VymoTheme.spacing
    val shape = RoundedCornerShape(VymoTheme.shapes.cornerMedium)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface, shape)
            .border(VymoTheme.shapes.borderThin, colors.primary, shape)
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Text(
            text = stringResource(R.string.lead_form_submitted_title),
            style = VymoTheme.typography.label,
            color = colors.primary,
            modifier = Modifier.semantics {
                heading()
                liveRegion = LiveRegionMode.Polite
            },
        )
        values.forEach { item ->
            Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
                Text(
                    text = item.label,
                    style = VymoTheme.typography.caption,
                    color = colors.onSurfaceMuted,
                )
                Text(
                    text = item.value,
                    style = VymoTheme.typography.body,
                    color = colors.onSurface,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LeadSubmissionSummaryPreview() {
    VymoTheme {
        LeadSubmissionSummary(
            values = listOf(
                SubmittedValue("Full name", "Jane Doe"),
                SubmittedValue("Email", "jane@example.com"),
                SubmittedValue("Lead type", "Individual"),
            ),
        )
    }
}
