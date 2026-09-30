package com.tlw.vymo.assignment.lead

import com.tlw.vymo.assignment.designsystem.form.CHECKED
import com.tlw.vymo.assignment.designsystem.form.FormValues
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LeadFormValidatorTest {

    private val validIndividual: FormValues = mapOf(
        LeadFields.FULL_NAME to "Jane Doe",
        LeadFields.EMAIL to "jane@example.com",
        LeadFields.LEAD_TYPE to LeadTypes.INDIVIDUAL,
        LeadFields.PHONE to "9876543210",
        LeadFields.CONSENT to CHECKED,
    )

    private fun errorsFor(values: FormValues) = validateForm(LeadFormConfig, values)

    @Test
    fun `valid individual lead has no errors`() {
        assertTrue(errorsFor(validIndividual).isEmpty())
    }

    @Test
    fun `empty form reports every required field`() {
        val errors = errorsFor(emptyMap())

        assertEquals(
            setOf(
                LeadFields.FULL_NAME,
                LeadFields.EMAIL,
                LeadFields.LEAD_TYPE,
                LeadFields.PHONE,
                LeadFields.CONSENT,
            ),
            errors.keys,
        )
    }

    @Test
    fun `blank name counts as missing`() {
        val errors = errorsFor(validIndividual + (LeadFields.FULL_NAME to "   "))

        assertEquals("Full name is required", errors[LeadFields.FULL_NAME])
    }

    @Test
    fun `malformed email is rejected`() {
        listOf("jane", "jane@", "jane@example", "@example.com", "jane doe@example.com").forEach {
            val errors = errorsFor(validIndividual + (LeadFields.EMAIL to it))
            assertEquals("for '$it'", "Enter a valid email address", errors[LeadFields.EMAIL])
        }
    }

    @Test
    fun `email with surrounding spaces is accepted`() {
        val errors = errorsFor(validIndividual + (LeadFields.EMAIL to " jane@example.com "))

        assertNull(errors[LeadFields.EMAIL])
    }

    @Test
    fun `phone must be exactly ten digits`() {
        listOf("98765", "98765432100", "98765-4321", "+919876543", "abcdefghij").forEach {
            val errors = errorsFor(validIndividual + (LeadFields.PHONE to it))
            assertEquals("for '$it'", "Enter a 10-digit number", errors[LeadFields.PHONE])
        }
    }

    @Test
    fun `company name is not required for individuals`() {
        assertFalse(LeadFields.COMPANY_NAME in errorsFor(validIndividual))
    }

    @Test
    fun `company name is required once lead type is company`() {
        val errors = errorsFor(validIndividual + (LeadFields.LEAD_TYPE to LeadTypes.COMPANY))

        assertEquals("Company name is required", errors[LeadFields.COMPANY_NAME])
    }

    @Test
    fun `company lead with a company name is valid`() {
        val values = validIndividual +
            (LeadFields.LEAD_TYPE to LeadTypes.COMPANY) +
            (LeadFields.COMPANY_NAME to "Acme")

        assertTrue(errorsFor(values).isEmpty())
    }

    @Test
    fun `notes are optional`() {
        assertNull(errorsFor(validIndividual + (LeadFields.NOTES to ""))[LeadFields.NOTES])
    }

    @Test
    fun `notes up to 200 characters are fine`() {
        val errors = errorsFor(validIndividual + (LeadFields.NOTES to "a".repeat(200)))

        assertNull(errors[LeadFields.NOTES])
    }

    @Test
    fun `notes over 200 characters are rejected`() {
        val errors = errorsFor(validIndividual + (LeadFields.NOTES to "a".repeat(201)))

        assertEquals("Use 200 characters or fewer (201/200)", errors[LeadFields.NOTES])
    }

    @Test
    fun `consent must be ticked`() {
        val errors = errorsFor(validIndividual - LeadFields.CONSENT)

        assertEquals("Please tick this box to continue", errors[LeadFields.CONSENT])
    }

    @Test
    fun `only the first failing rule is reported`() {
        val errors = errorsFor(validIndividual + (LeadFields.EMAIL to ""))

        assertEquals("Email is required", errors[LeadFields.EMAIL])
    }
}
