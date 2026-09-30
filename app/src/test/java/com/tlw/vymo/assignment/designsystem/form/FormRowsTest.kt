package com.tlw.vymo.assignment.designsystem.form

import org.junit.Assert.assertEquals
import org.junit.Test

class FormRowsTest {

    private val name = FieldConfig("name", FieldType.Text, "Name")
    private val email = FieldConfig("email", FieldType.Email, "Email")
    private val type = FieldConfig("type", FieldType.Select, "Type")
    private val phone = FieldConfig("phone", FieldType.Text, "Phone")
    private val notes = FieldConfig("notes", FieldType.TextArea, "Notes")
    private val consent = FieldConfig("consent", FieldType.Checkbox, "Consent")

    @Test
    fun `single column puts every field on its own row`() {
        val rows = packRows(listOf(name, email, notes), columns = 1)

        assertEquals(listOf(listOf(name), listOf(email), listOf(notes)), rows)
    }

    @Test
    fun `two columns pair up half width fields`() {
        val rows = packRows(listOf(name, email, type, phone), columns = 2)

        assertEquals(listOf(listOf(name, email), listOf(type, phone)), rows)
    }

    @Test
    fun `full width field gets its own row`() {
        val rows = packRows(listOf(name, email, notes, consent), columns = 2)

        assertEquals(listOf(listOf(name, email), listOf(notes), listOf(consent)), rows)
    }

    @Test
    fun `half field before a full one stays alone in its row`() {
        val rows = packRows(listOf(name, email, phone, notes), columns = 2)

        assertEquals(listOf(listOf(name, email), listOf(phone), listOf(notes)), rows)
    }

    @Test
    fun `explicit width overrides the default for the type`() {
        val wideName = name.copy(width = FieldWidth.Full)

        val rows = packRows(listOf(wideName, email), columns = 2)

        assertEquals(listOf(listOf(wideName), listOf(email)), rows)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `zero columns is rejected`() {
        packRows(listOf(name), columns = 0)
    }
}
