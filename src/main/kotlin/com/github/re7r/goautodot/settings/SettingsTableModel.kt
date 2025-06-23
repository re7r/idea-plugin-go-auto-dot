package com.github.re7r.goautodot.settings

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.swing.table.AbstractTableModel

class SettingsTableModel : AbstractTableModel() {

    private val columnNames = arrayOf("Import Path", "")
    private var saved: List<String> = listOf()
    private var data: MutableList<Entry> = mutableListOf()

    data class Entry(
        var path: String,
        var state: EntryState = EntryState(),
    ) {
        fun validate(): Boolean {
            state.isValid = true
            state.errors.clear()

            if (!path.isBlank() && !isValidPath(path)) {
                state.isValid = false
                state.errors += "Invalid import path"
            }

            return state.isValid
        }

        private fun isValidPath(path: String): Boolean {
            val segment = """(?!\.)(?!.*\.\z)[\p{L}\p{N}_~\-][\p{L}\p{N}._~\-]*"""
            val regex = Regex("""^$segment(?:/$segment)*$""")
            return regex.matches(path)
        }
    }

    data class EntryState(
        var isModified: Boolean = false,
        var isValid: Boolean = true,
        var errors: MutableList<String> = mutableListOf(),
    )

    override fun getRowCount() = data.size
    override fun getColumnCount() = 2
    override fun getColumnName(column: Int) = columnNames[column]
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return when (columnIndex) {
            0 -> data[rowIndex].path
            1 -> "Delete"
            else -> throw IllegalArgumentException("Invalid column index")
        }
    }

    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        if (columnIndex == 1) return

        val entry = data[rowIndex]
        val newValue = value?.toString() ?: ""
        var changed = false

        if (columnIndex == 0) {
            if (entry.path != newValue) {
                entry.path = newValue
                changed = true
            }
        }

        if (changed) {
            entry.validate()
            entry.state.isModified = (rowIndex >= saved.size) || (saved[rowIndex] != entry.path)
            fireTableCellUpdated(rowIndex, columnIndex)
        }
    }

    fun entries(): ImmutableList<Entry> {
        return data.toImmutableList()
    }

    fun list(): ImmutableList<String> {
        return data
            .map { it.path }
            .filter { it.isNotBlank() }
            .toImmutableList()
    }

    fun load(list: List<String>) {
        data = list.mapIndexed { index, it ->
            Entry(
                it, EntryState(
                    isModified = (index >= saved.size) || (saved[index] != it)
                )
            )
        }.toMutableList()
        fireTableDataChanged()
    }

    fun sync() {
        this.saved = list()

        for (entry in data) {
            entry.state.isModified = false
        }
    }

    fun addRow() {
        data.add(Entry(""))
        fireTableRowsInserted(data.size - 1, data.size - 1)
    }

    fun removeRow(rowIndex: Int) {
        if (rowIndex in data.indices) {
            data.removeAt(rowIndex)
            fireTableRowsDeleted(rowIndex, rowIndex)
        }
    }

    fun hasModifications(): Boolean {
        return list() != saved
    }

    fun getEntry(rowIndex: Int): Entry = data[rowIndex]
}