package com.tlw.vymo.assignment.lead

import com.tlw.vymo.assignment.designsystem.form.CHECKED
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LeadFormViewModelTest {

    private val viewModel = LeadFormViewModel()
    private val state get() = viewModel.state.value

    private fun fillValidIndividual() {
        viewModel.onValueChange(LeadFields.FULL_NAME, "Jane Doe")
        viewModel.onValueChange(LeadFields.EMAIL, "jane@example.com")
        viewModel.onValueChange(LeadFields.LEAD_TYPE, LeadTypes.INDIVIDUAL)
        viewModel.onValueChange(LeadFields.PHONE, "9876543210")
        viewModel.onValueChange(LeadFields.CONSENT, CHECKED)
    }

    @Test
    fun `no errors are shown before the user leaves a field`() {
        viewModel.onValueChange(LeadFields.EMAIL, "jane")

        assertTrue(state.errors.isEmpty())
    }

    @Test
    fun `leaving a field shows only that field's error`() {
        viewModel.onValueChange(LeadFields.EMAIL, "jane")
        viewModel.onFieldFocusLost(LeadFields.EMAIL)

        assertEquals(setOf(LeadFields.EMAIL), state.errors.keys)
    }

    @Test
    fun `fixing a touched field clears its error`() {
        viewModel.onValueChange(LeadFields.EMAIL, "jane")
        viewModel.onFieldFocusLost(LeadFields.EMAIL)
        viewModel.onValueChange(LeadFields.EMAIL, "jane@example.com")

        assertNull(state.errors[LeadFields.EMAIL])
    }

    @Test
    fun `submitting an invalid form shows every error and nothing is submitted`() {
        viewModel.onSubmit()

        assertEquals(5, state.errors.size)
        assertNull(state.submission)
    }

    @Test
    fun `submitting a valid form produces the submitted values`() {
        fillValidIndividual()

        viewModel.onSubmit()

        assertTrue(state.errors.isEmpty())
        assertEquals(
            listOf(
                SubmittedValue("Full name", "Jane Doe"),
                SubmittedValue("Email", "jane@example.com"),
                SubmittedValue("Lead type", "Individual"),
                SubmittedValue("Phone", "9876543210"),
                SubmittedValue("Notes", "—"),
                SubmittedValue("I agree to be contacted about this enquiry", "Yes"),
            ),
            state.submission,
        )
    }

    @Test
    fun `switching to company after a submit attempt flags the company name`() {
        fillValidIndividual()
        viewModel.onSubmit()

        viewModel.onValueChange(LeadFields.LEAD_TYPE, LeadTypes.COMPANY)

        assertEquals(setOf(LeadFields.COMPANY_NAME), state.errors.keys)
    }

    @Test
    fun `hidden company name is left out of the submission`() {
        fillValidIndividual()
        viewModel.onValueChange(LeadFields.LEAD_TYPE, LeadTypes.COMPANY)
        viewModel.onValueChange(LeadFields.COMPANY_NAME, "Acme")
        viewModel.onValueChange(LeadFields.LEAD_TYPE, LeadTypes.INDIVIDUAL)

        viewModel.onSubmit()

        val labels = state.submission.orEmpty().map { it.label }
        assertTrue("Company name" !in labels)
    }

    @Test
    fun `editing after a submit clears the old submission`() {
        fillValidIndividual()
        viewModel.onSubmit()

        viewModel.onValueChange(LeadFields.NOTES, "Call after 5")

        assertNull(state.submission)
    }

    @Test
    fun `edit closes the result and keeps the values`() {
        fillValidIndividual()
        viewModel.onSubmit()
        val submittedValues = state.values

        viewModel.onEditSubmission()

        assertNull(state.submission)
        assertEquals(submittedValues, state.values)
    }

    @Test
    fun `new lead clears values and errors`() {
        fillValidIndividual()
        viewModel.onFieldFocusLost(LeadFields.FULL_NAME)
        viewModel.onSubmit()

        viewModel.onStartNewLead()

        assertTrue(state.values.isEmpty())
        assertTrue(state.errors.isEmpty())
        assertEquals(false, state.submitAttempted)
        assertNull(state.submission)
    }

    @Test
    fun `fields left before a new lead don't show errors afterwards`() {
        viewModel.onFieldFocusLost(LeadFields.EMAIL)
        viewModel.onStartNewLead()

        viewModel.onValueChange(LeadFields.EMAIL, "jane")

        assertTrue(state.errors.isEmpty())
    }
}
