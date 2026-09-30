package com.tlw.vymo.assignment.lead

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.tlw.vymo.assignment.designsystem.form.CHECKED
import com.tlw.vymo.assignment.designsystem.form.FieldConfig
import com.tlw.vymo.assignment.designsystem.form.FieldType
import com.tlw.vymo.assignment.designsystem.form.FormValues
import com.tlw.vymo.assignment.designsystem.form.valueOf
import com.tlw.vymo.assignment.designsystem.form.visibleFields
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Immutable
data class SubmittedValue(val label: String, val value: String)

@Immutable
data class LeadFormUiState(
    val fields: List<FieldConfig>,
    val values: FormValues = emptyMap(),
    /** Errors the user should see right now; a subset of what the validator returns. */
    val errors: Map<String, String> = emptyMap(),
    val submitAttempted: Boolean = false,
    val submission: List<SubmittedValue>? = null,
)

class LeadFormViewModel(
    private val config: List<FieldConfig> = LeadFormConfig,
) : ViewModel() {

    private var touched = emptySet<String>()

    private val _state = MutableStateFlow(LeadFormUiState(fields = config))
    val state: StateFlow<LeadFormUiState> = _state.asStateFlow()

    fun onValueChange(name: String, value: String) {
        _state.update { current ->
            current.copy(values = current.values + (name to value), submission = null)
                .withVisibleErrors()
        }
    }

    fun onFieldFocusLost(name: String) {
        touched = touched + name
        _state.update { it.withVisibleErrors() }
    }

    fun onSubmit() {
        _state.update { current ->
            val errors = validateForm(config, current.values)
            current.copy(
                submitAttempted = true,
                errors = errors,
                submission = if (errors.isEmpty()) current.values.toSubmission() else null,
            )
        }
    }

    /** Closes the submitted view and goes back to the filled form. */
    fun onEditSubmission() {
        _state.update { it.copy(submission = null) }
    }

    /** Clears everything for the next lead. */
    fun onStartNewLead() {
        touched = emptySet()
        _state.value = LeadFormUiState(fields = config)
    }

    // A field shows its error once the user has left it, or after any submit attempt.
    private fun LeadFormUiState.withVisibleErrors(): LeadFormUiState {
        val all = validateForm(config, values)
        return copy(errors = all.filterKeys { submitAttempted || it in touched })
    }

    private fun FormValues.toSubmission(): List<SubmittedValue> =
        config.visibleFields(this).map { field ->
            SubmittedValue(label = field.label, value = displayValue(field, valueOf(field)))
        }

    private fun displayValue(field: FieldConfig, value: String): String = when {
        field.type == FieldType.Checkbox -> if (value == CHECKED) "Yes" else "No"
        field.type == FieldType.Select -> field.options.firstOrNull { it.value == value }?.label ?: value
        value.isBlank() -> "—"
        else -> value.trim()
    }
}
