package com.github.re7r.goautodot.listeners

import com.github.re7r.goautodot.GoAutoDotImportProcessor
import com.goide.psi.GoFile
import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager

class SaveListener(private val project: Project) : FileDocumentManagerListener {

    private val pdm = PsiDocumentManager.getInstance(project)

    override fun beforeDocumentSaving(document: Document) {
        val file = pdm.getPsiFile(document) as? GoFile ?: return

        ApplicationManager.getApplication().invokeLater {
            pdm.commitDocument(document)
            WriteCommandAction.runWriteCommandAction(project) {
                if (GoAutoDotImportProcessor.processFile(project, file)) {
                    OptimizeImportsProcessor(project, file).run()
                }
            }
        }
    }
}