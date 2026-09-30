package com.tlw.vymo.assignment.designsystem.form

import androidx.compose.runtime.Immutable
import com.tlw.vymo.assignment.designsystem.atoms.VymoSelectOption

enum class FieldType { Text, Email, Select, TextArea, Checkbox }

/** How much of a row a field takes once the form has more than one column. */
enum class FieldWidth { Half, Full }

enum class FieldKeyboard { Default, Phone }

sealed interface Validation {
    data object Required : Validation
    data object Email : Validation
    data class ExactDigits(val count: Int) : Validation
    data class MaxLength(val max: Int) : Validation
}

/** The field is shown only while [field] currently holds [equals]. */
data class VisibleWhen(val field: String, val equals: String)

@Immutable
data class FieldConfig(
    val name: String,
    val type: FieldType,
    val label: String,
    val validations: List<Validation> = emptyList(),
    val hint: String? = null,
    val placeholder: String? = null,
    val options: List<VymoSelectOption> = emptyList(),
    val visibleWhen: VisibleWhen? = null,
    val keyboard: FieldKeyboard = FieldKeyboard.Default,
    val width: FieldWidth = when (type) {
        FieldType.TextArea, FieldType.Checkbox -> FieldWidth.Full
        else -> FieldWidth.Half
    },
) {
    val isRequired: Boolean
        get() = Validation.Required in validations
}

/** Current values keyed by [FieldConfig.name]. Checkboxes store [CHECKED] or nothing. */
typealias FormValues = Map<String, String>

const val CHECKED = "true"

fun FormValues.valueOf(field: FieldConfig): String = this[field.name].orEmpty()

fun FieldConfig.isVisible(values: FormValues): Boolean =
    visibleWhen?.let { values[it.field] == it.equals } ?: true

fun List<FieldConfig>.visibleFields(values: FormValues): List<FieldConfig> =
    filter { it.isVisible(values) }
