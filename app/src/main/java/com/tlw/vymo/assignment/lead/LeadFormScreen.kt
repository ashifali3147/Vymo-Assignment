package com.tlw.vymo.assignment.lead

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tlw.vymo.assignment.R
import com.tlw.vymo.assignment.designsystem.atoms.VymoButton
import com.tlw.vymo.assignment.designsystem.form.VymoForm
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme
import com.tlw.vymo.assignment.designsystem.tokens.VymoBreakpoints

@Composable
fun LeadFormRoute(viewModel: LeadFormViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LeadFormScreen(
        state = state,
        onValueChange = viewModel::onValueChange,
        onFieldFocusLost = viewModel::onFieldFocusLost,
        onSubmit = viewModel::onSubmit,
        onEditSubmission = viewModel::onEditSubmission,
        onStartNewLead = viewModel::onStartNewLead,
    )
}

@Composable
fun LeadFormScreen(
    state: LeadFormUiState,
    onValueChange: (String, String) -> Unit,
    onFieldFocusLost: (String) -> Unit,
    onSubmit: () -> Unit,
    onEditSubmission: () -> Unit,
    onStartNewLead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = VymoTheme.spacing
    val focusManager = LocalFocusManager.current
    val submission = state.submission
    val showResultPage = submission != null

    // Separate scroll states so going back to the form keeps its position.
    val formScrollState = rememberScrollState()
    val resultScrollState = rememberScrollState()
    val scrollState = if (showResultPage) resultScrollState else formScrollState

    BackHandler(enabled = showResultPage, onBack = onEditSubmission)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = VymoTheme.colors.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (showResultPage) {
                BottomBar {
                    LeadSubmissionActions(
                        onEdit = onEditSubmission,
                        onNewLead = onStartNewLead,
                        modifier = Modifier.widthIn(max = VymoBreakpoints.MaxContentWidth),
                    )
                }
            } else {
                SubmitBar(
                    errorCount = if (state.submitAttempted) state.errors.size else 0,
                    onSubmit = {
                        focusManager.clearFocus()
                        onSubmit()
                    },
                )
            }
        },
    ) { innerPadding ->
        // The bottom bar grows with the keyboard, so innerPadding keeps the
        // scrollable area above it and the focused field is scrolled into view.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = spacing.lg, vertical = spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = VymoBreakpoints.MaxContentWidth)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.xl),
            ) {
                if (showResultPage) {
                    LeadSubmissionResult(values = submission)
                } else {
                    Header()
                    VymoForm(
                        fields = state.fields,
                        values = state.values,
                        errors = state.errors,
                        onValueChange = onValueChange,
                        onFieldFocusLost = onFieldFocusLost,
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column(verticalArrangement = Arrangement.spacedBy(VymoTheme.spacing.xs)) {
        Text(
            text = stringResource(R.string.lead_form_title),
            style = VymoTheme.typography.title,
            color = VymoTheme.colors.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(R.string.lead_form_subtitle),
            style = VymoTheme.typography.caption,
            color = VymoTheme.colors.onSurfaceMuted,
        )
    }
}

@Composable
private fun BottomBar(content: @Composable BoxWithConstraintsScope.() -> Unit) {
    val spacing = VymoTheme.spacing

    Surface(color = VymoTheme.colors.surface) {
        Column {
            HorizontalDivider(color = VymoTheme.colors.disabled)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
                        ),
                    )
                    .padding(horizontal = spacing.lg, vertical = spacing.md),
                contentAlignment = Alignment.Center,
                content = content,
            )
        }
    }
}

@Composable
private fun SubmitBar(errorCount: Int, onSubmit: () -> Unit) {
    val spacing = VymoTheme.spacing

    BottomBar {
        val barModifier = Modifier
            .widthIn(max = VymoBreakpoints.MaxContentWidth)
            .fillMaxWidth()
        val errorText: @Composable (Modifier) -> Unit = { textModifier ->
            if (errorCount > 0) {
                Text(
                    text = pluralStringResource(
                        R.plurals.lead_form_error_count,
                        errorCount,
                        errorCount,
                    ),
                    style = VymoTheme.typography.caption,
                    color = VymoTheme.colors.error,
                    modifier = textModifier,
                )
            }
        }
        val label = stringResource(R.string.lead_form_submit)

        // On wide screens height is usually the scarce side (landscape),
        // so the message sits beside the button instead of above it.
        if (maxWidth >= VymoBreakpoints.TwoColumnMinWidth) {
            Row(
                modifier = barModifier,
                horizontalArrangement = Arrangement.spacedBy(spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                errorText(Modifier.weight(1f))
                if (errorCount == 0) Spacer(Modifier.weight(1f))
                VymoButton(
                    label = label,
                    onClick = onSubmit,
                    modifier = Modifier.widthIn(min = SubmitButtonMinWidth),
                )
            }
        } else {
            Column(
                modifier = barModifier,
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                errorText(Modifier)
                VymoButton(
                    label = label,
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private val SubmitButtonMinWidth = 200.dp

@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
private fun LeadFormCompactPreview() {
    VymoTheme {
        LeadFormScreen(
            state = LeadFormUiState(fields = LeadFormConfig),
            onValueChange = { _, _ -> },
            onFieldFocusLost = {},
            onSubmit = {},
            onEditSubmission = {},
            onStartNewLead = {},
        )
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_TABLET)
@Composable
private fun LeadFormExpandedPreview() {
    VymoTheme {
        LeadFormScreen(
            state = LeadFormUiState(fields = LeadFormConfig),
            onValueChange = { _, _ -> },
            onFieldFocusLost = {},
            onSubmit = {},
            onEditSubmission = {},
            onStartNewLead = {},
        )
    }
}
