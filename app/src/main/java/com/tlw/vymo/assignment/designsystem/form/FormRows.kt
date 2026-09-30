package com.tlw.vymo.assignment.designsystem.form

/**
 * Groups fields into rows for a layout with [columns] columns.
 *
 * Half-width fields fill a row left to right. A full-width field always gets a row
 * of its own, so a half field left waiting before it ends up alone in its row.
 */
fun packRows(fields: List<FieldConfig>, columns: Int): List<List<FieldConfig>> {
    require(columns >= 1) { "columns must be at least 1, was $columns" }
    if (columns == 1) return fields.map { listOf(it) }

    val rows = mutableListOf<List<FieldConfig>>()
    var current = mutableListOf<FieldConfig>()

    fun flush() {
        if (current.isNotEmpty()) {
            rows += current
            current = mutableListOf()
        }
    }

    for (field in fields) {
        if (field.width == FieldWidth.Full) {
            flush()
            rows += listOf(field)
        } else {
            current += field
            if (current.size == columns) flush()
        }
    }
    flush()
    return rows
}
