package com.github.re7r.goautodot.settings

import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.Color
import java.awt.Component
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.UIManager
import javax.swing.table.TableCellRenderer

class SettingsCellRenderer : JLabel(), TableCellRenderer {

    private val defaultColor = UIManager.getColor("Table.foreground")
    private val modifiedColor = JBColor(Color(70, 127, 249), Color(107, 155, 250))
    private val invalidColor = JBColor.RED
    private val buttonRenderer = JButton()

    init {
        isOpaque = true
        horizontalAlignment = LEFT
    }

    override fun getTableCellRendererComponent(
        table: JTable, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int
    ): Component {
        val model = table.model as SettingsTableModel
        val entry = model.getEntry(row)

        border = JBUI.Borders.empty(0, 10)

        background = when (isSelected) {
            true -> table.selectionBackground
            false -> UIManager.getColor("Table.background")
        }

        foreground = when {
            !entry.state.isValid -> invalidColor
            entry.state.isModified -> modifiedColor
            else -> defaultColor
        }

        val messages = entry.state.errors.toMutableList()

        if (messages.isEmpty()) {
            when {
                entry.state.isModified -> messages += "Unsaved entry"
            }
        }

        val tooltip = messages.joinToString(". ")

        if (column == 1) {
            buttonRenderer.text = value?.toString() ?: "Delete"
            buttonRenderer.toolTipText = tooltip
            buttonRenderer.foreground = foreground
            buttonRenderer.background = UIManager.getColor("Button.background")
            buttonRenderer.font = buttonRenderer.font.deriveFont(
                maxOf(buttonRenderer.font.size - 3, 11).toFloat()
            )

            return buttonRenderer
        } else {
            text = value?.toString() ?: ""
            toolTipText = tooltip
            return this
        }
    }
}
