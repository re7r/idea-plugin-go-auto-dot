package com.github.re7r.goautodot.settings

import com.github.re7r.goautodot.schemes.Scheme
import com.github.re7r.goautodot.schemes.SchemeState
import com.github.re7r.goautodot.schemes.SchemesManager
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.*

class SettingsPanel(private val manager: SchemesManager) : JPanel(BorderLayout()) {

    private val model = SettingsTableModel()
    private val table = JBTable(model)

    init {
        table.setDefaultRenderer(Any::class.java, SettingsCellRenderer())
        table.putClientProperty("terminateEditOnFocusLost", true)
        (table.getDefaultEditor(String::class.java) as? DefaultCellEditor)?.clickCountToStart = 1

        table.columnModel.getColumn(1).cellEditor = SettingsDeleteButtonEditor(table, model)
        table.columnModel.getColumn(1).preferredWidth = 80
        table.columnModel.getColumn(1).maxWidth = 80
        table.columnModel.getColumn(1).minWidth = 80

        val top = JPanel().apply {
            border = JBUI.Borders.emptyBottom(12)
            layout = BoxLayout(this, BoxLayout.X_AXIS)
        }

        top.add(Box.createHorizontalGlue())
        top.add(JButton("Add").apply {
            addActionListener {
                if (table.isEditing) table.cellEditor.stopCellEditing()
                this@SettingsPanel.model.addRow()
                table.editCellAt(this@SettingsPanel.model.rowCount - 1, 0)
                table.editorComponent?.requestFocusInWindow()
            }
        })

        add(top, BorderLayout.NORTH)
        add(JScrollPane(table), BorderLayout.CENTER)
    }

    fun isModified(): Boolean {
        return model.hasModifications()
    }

    fun apply() {
        for (entry in model.entries()) {
            if (!entry.state.isValid) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid value found: '${entry.path}'. Please correct it.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                )
                return
            }
        }

        manager.getScheme().let {
            it.state.packages.clear()
            it.state.packages.addAll(model.list().distinct())
        }

        reset()
    }

    fun reset(scheme: Scheme? = null) {
        refresh(scheme?.state ?: manager.getScheme().state)
        model.sync()
    }

    fun refresh(state: SchemeState) {
        if (table.isEditing) table.cellEditor.stopCellEditing()
        model.load(state.packages)
    }
}