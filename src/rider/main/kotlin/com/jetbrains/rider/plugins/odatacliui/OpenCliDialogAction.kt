package com.jetbrains.rider.plugins.odatacliui

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.openapi.rd.util.lifetime
import com.jetbrains.rider.plugins.odatacliui.extensions.entityForAction
import com.jetbrains.rider.projectView.workspace.isProject
import com.jetbrains.rider.projectView.workspace.isWebReferenceFolder

class OpenCliDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        project.lifetime.launchOnUi {
            val dialog = CliDialog()
            dialog.show()
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val entity = e.entityForAction
        e.presentation.isVisible = entity.isWebReferenceFolder() || entity.isProject()
    }
}