package com.github.re7r.goautodot

import com.github.re7r.goautodot.schemes.SchemesManager
import com.goide.intentions.GoAddDotImportAliasIntention
import com.goide.psi.GoFile
import com.goide.psi.GoImportSpec
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project

object GoAutoDotImportProcessor {

    private val intention = GoAddDotImportAliasIntention()

    fun processFile(project: Project, file: GoFile): Boolean {
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return false
        var updated = false

        file.imports.forEach { spec ->
            if (processImport(project, editor, spec)) {
                updated = true
            }
        }

        return updated
    }

    fun processImport(project: Project, editor: Editor, spec: GoImportSpec): Boolean {
        if (!spec.isValid || spec.alias != null) {
            return false
        }

        val manager = project.getService(SchemesManager::class.java)
        if (manager.getScheme().state.packages.contains(spec.path)) {
            intention.invoke(project, editor, spec)
            return true
        }

        return false
    }
}
