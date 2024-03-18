package com.jetbrains.rider.plugins.odatacliui.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.ui.Messages
import com.jetbrains.rider.plugins.odatacliui.dialogs.CliDialog
import com.jetbrains.rider.plugins.odatacliui.extensions.entityForAction
import com.jetbrains.rider.plugins.odatacliui.models.CliDialogModel
import com.jetbrains.rider.projectView.workspace.isProject
import com.jetbrains.rider.projectView.workspace.isWebReferenceFolder

class OpenCliDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val dialogModel = CliDialogModel(e)
        project.lifetime.launchOnUi {
            val dialog = CliDialog(dialogModel)
            if (dialog.showAndGet()) {
                Messages.showInfoMessage(dialogModel.buildCommand().commandLineString, "Command String")
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val entity = e.entityForAction
        e.presentation.isVisible = entity.isWebReferenceFolder() || entity.isProject()
    }
}