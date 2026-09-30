package com.tlw.vymo.assignment.lead

import com.tlw.vymo.assignment.designsystem.form.CHECKED
import com.tlw.vymo.assignment.designsystem.form.FieldConfig
import com.tlw.vymo.assignment.designsystem.form.FieldType
import com.tlw.vymo.assignment.designsystem.form.FormValues
import com.tlw.vymo.assignment.designsystem.form.Validation
import com.tlw.vymo.assignment.designsystem.form.isVisible
import com.tlw.vymo.assignment.designsystem.form.valueOf

private val EmailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

/**
 * Returns the first failing rule's message for every visible field, keyed by field name.
 * An empty map means the form can be submitted.
 *
 * Hidden fields are skipped, so a field that is conditionally shown is only
 * required while it is on screen.
 */
fun validateForm(config: List<FieldConfig>, values: FormValues): Map<String, String> =
    config
        .filter { it.isVisible(values) }
        .mapNotNull { field ->
            validateField(field, values.valueOf(field))?.let { field.name to it }
        }
        .toMap()

private fun validateField(field: FieldConfig, rawValue: String): String? {
    val value = rawValue.trim()
    val isEmpty = if (field.type == FieldType.Checkbox) rawValue != CHECKED else value.isEmpty()

    if (isEmpty) {
        return if (field.isRequired) requiredMessage(field) else null
    }

    return field.validations.firstNotNullOfOrNull { rule ->
        when (rule) {
            Validation.Required -> null
            Validation.Email ->
                "Enter a valid email address".takeUnless { EmailPattern.matches(value) }

            is Validation.ExactDigits ->
                "Enter a ${rule.count}-digit number"
                    .takeUnless { value.length == rule.count && value.all(Char::isDigit) }

            is Validation.MaxLength ->
                "Use ${rule.max} characters or fewer (${rawValue.length}/${rule.max})"
                    .takeUnless { rawValue.length <= rule.max }
        }
    }
}

private fun requiredMessage(field: FieldConfig): String = when (field.type) {
    FieldType.Checkbox -> "Please tick this box to continue"
    FieldType.Select -> "Select ${field.label.lowercase()}"
    else -> "${field.label} is required"
}
