package com.github.re7r.goautodot

import com.github.re7r.goautodot.listeners.ChangeListener
import com.github.re7r.goautodot.listeners.SaveListener
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.psi.PsiManager

class GoAutoDotStartupActivity : ProjectActivity {
    
    override suspend fun execute(project: Project) {
        @Suppress("IncorrectParentDisposable")
        val connection = project.messageBus.connect(project)

        connection.subscribe(
            FileDocumentManagerListener.TOPIC,
            SaveListener(project)
        )

        @Suppress("IncorrectParentDisposable")
        PsiManager.getInstance(project).addPsiTreeChangeListener(
            ChangeListener(project),
            project
        )
    }
}
