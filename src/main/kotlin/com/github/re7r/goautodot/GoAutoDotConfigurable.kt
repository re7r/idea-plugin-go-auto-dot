package com.github.re7r.goautodot

import com.github.re7r.goautodot.schemes.SchemesManager
import com.github.re7r.goautodot.schemes.SchemesPanel
import com.github.re7r.goautodot.settings.SettingsPanel
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBBox
import java.awt.Component
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel

class GoAutoDotConfigurable(private val project: Project) : SearchableConfigurable {

    private var schemes: SchemesPanel? = null
    private var settings: SettingsPanel? = null

    override fun getId() = "goautodot"
    override fun getDisplayName() = "Go Auto Dot"

    override fun createComponent(): JComponent? {
        val manager = project.getService(SchemesManager::class.java)

        settings = SettingsPanel(manager).apply {
            alignmentX = Component.LEFT_ALIGNMENT
        }

        schemes = SchemesPanel(manager).apply {
            alignmentX = Component.LEFT_ALIGNMENT
            maximumSize = Dimension(350, preferredSize.height)
            setSeparatorVisible(false)

            onSchemeChange { scheme ->
                settings!!.reset(scheme)
            }

            onStateUpdate { state ->
                settings!!.refresh(state)
            }
        }

        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(schemes)
            add(JBBox.createVerticalStrut(-42))
            add(settings)
        }
    }

    override fun isModified(): Boolean {
        return schemes!!.isModified() || settings!!.isModified()
    }

    override fun apply() {
        schemes!!.apply()
        settings!!.apply()
    }

    override fun reset() {
        schemes!!.reset()
        settings!!.reset()
    }

    override fun disposeUIResources() {
        schemes = null
        settings = null
    }
}