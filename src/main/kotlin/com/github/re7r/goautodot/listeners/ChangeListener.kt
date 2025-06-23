package com.github.re7r.goautodot.listeners

import com.github.re7r.goautodot.GoAutoDotImportProcessor
import com.goide.psi.GoImportSpec
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent

class ChangeListener(private val project: Project) : PsiTreeChangeAdapter() {

    private val pdm = PsiDocumentManager.getInstance(project)

    override fun childAdded(event: PsiTreeChangeEvent) = process(event)
    override fun childReplaced(event: PsiTreeChangeEvent) = process(event)

    private fun process(event: PsiTreeChangeEvent) {
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        val new = event.child as? GoImportSpec ?: return
        val old = event.oldChild as? GoImportSpec

        if (old?.alias == "." && new.alias == null) {
            return
        }

        ApplicationManager.getApplication().invokeLater {
            if (project.isDisposed || !new.isValid) return@invokeLater
            pdm.commitDocument(editor.document)

            CommandProcessor.getInstance().runUndoTransparentAction {
                WriteCommandAction.runWriteCommandAction(project) {
                    GoAutoDotImportProcessor.processImport(project, editor, new)
                }
            }
        }
    }
}
