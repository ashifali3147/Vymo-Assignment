package com.tlw.vymo.assignment.lead

import com.tlw.vymo.assignment.designsystem.atoms.VymoSelectOption
import com.tlw.vymo.assignment.designsystem.form.FieldConfig
import com.tlw.vymo.assignment.designsystem.form.FieldKeyboard
import com.tlw.vymo.assignment.designsystem.form.FieldType
import com.tlw.vymo.assignment.designsystem.form.Validation
import com.tlw.vymo.assignment.designsystem.form.VisibleWhen

object LeadFields {
    const val FULL_NAME = "fullName"
    const val EMAIL = "email"
    const val LEAD_TYPE = "leadType"
    const val COMPANY_NAME = "companyName"
    const val PHONE = "phone"
    const val NOTES = "notes"
    const val CONSENT = "consent"
}

object LeadTypes {
    const val INDIVIDUAL = "individual"
    const val COMPANY = "company"
}

private const val NOTES_MAX_LENGTH = 200
private const val PHONE_DIGITS = 10

/** Field order here is the order on screen. */
val LeadFormConfig: List<FieldConfig> = listOf(
    FieldConfig(
        name = LeadFields.FULL_NAME,
        type = FieldType.Text,
        label = "Full name",
        placeholder = "Enter full name",
        validations = listOf(Validation.Required),
    ),
    FieldConfig(
        name = LeadFields.EMAIL,
        type = FieldType.Email,
        label = "Email",
        placeholder = "Enter email address",
        validations = listOf(Validation.Required, Validation.Email),
    ),
    FieldConfig(
        name = LeadFields.LEAD_TYPE,
        type = FieldType.Select,
        label = "Lead type",
        placeholder = "Select lead type",
        options = listOf(
            VymoSelectOption(LeadTypes.INDIVIDUAL, "Individual"),
            VymoSelectOption(LeadTypes.COMPANY, "Company"),
        ),
        validations = listOf(Validation.Required),
    ),
    FieldConfig(
        name = LeadFields.COMPANY_NAME,
        type = FieldType.Text,
        label = "Company name",
        placeholder = "Enter company name",
        visibleWhen = VisibleWhen(field = LeadFields.LEAD_TYPE, equals = LeadTypes.COMPANY),
        validations = listOf(Validation.Required),
    ),
    FieldConfig(
        name = LeadFields.PHONE,
        type = FieldType.Text,
        label = "Phone",
        placeholder = "Enter phone number",
        hint = "10 digits, no country code",
        keyboard = FieldKeyboard.Phone,
        validations = listOf(Validation.Required, Validation.ExactDigits(PHONE_DIGITS)),
    ),
    FieldConfig(
        name = LeadFields.NOTES,
        type = FieldType.TextArea,
        label = "Notes",
        hint = "Optional, up to $NOTES_MAX_LENGTH characters",
        validations = listOf(Validation.MaxLength(NOTES_MAX_LENGTH)),
    ),
    FieldConfig(
        name = LeadFields.CONSENT,
        type = FieldType.Checkbox,
        label = "I agree to be contacted about this enquiry",
        validations = listOf(Validation.Required),
    ),
)
