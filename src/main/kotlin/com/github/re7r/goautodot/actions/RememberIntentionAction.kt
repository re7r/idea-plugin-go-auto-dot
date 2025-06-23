package com.github.re7r.goautodot.actions

import com.github.re7r.goautodot.schemes.SchemesManager
import com.goide.intentions.GoBaseIntentionAction
import com.goide.psi.GoImportSpec
import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement

class RememberIntentionAction : GoBaseIntentionAction(), LowPriorityAction, Iconable {

    override fun getText() = "Remember dot as import alias"
    override fun getIcon(flags: Int) = AllIcons.Actions.MenuSaveall
    override fun getFamilyName() = getText()
    override fun startInWriteAction() = false

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        val spec = findImportSpec(element) ?: return false
        val manager = project.getService(SchemesManager::class.java)
        return !manager.getScheme().state.packages.contains(spec.path)
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val spec = findImportSpec(element) ?: return
        val manager = project.getService(SchemesManager::class.java)
        manager.getScheme().state.packages.add(spec.path)
    }

    private fun findImportSpec(element: PsiElement): GoImportSpec? {
        val spec = (element.parent as? GoImportSpec) ?: (element.parent.parent as? GoImportSpec)
        return if (spec?.isDot == true) spec else null
    }
}