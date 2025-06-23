package com.github.re7r.goautodot.suppressors

import com.github.re7r.goautodot.schemes.SchemesManager
import com.goide.psi.GoImportSpec
import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement

class RedundantImportInspectionSuppressor : InspectionSuppressor {

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> = emptyArray()

    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (toolId != "GoRedundantImportAlias") return false
        val importSpec = element.parent as? GoImportSpec ?: return false
        val manager = element.project.getService(SchemesManager::class.java)
        return manager.getScheme().state.packages.contains(importSpec.path)
    }
}