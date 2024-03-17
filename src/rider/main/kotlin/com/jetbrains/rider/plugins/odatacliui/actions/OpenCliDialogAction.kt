package com.jetbrains.rider.plugins.odatacliui.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.openapi.rd.util.lifetime
import com.jetbrains.rd.ide.model.protocolModel
import com.jetbrains.rider.plugins.odatacliui.dialogs.CliDialog
import com.jetbrains.rider.plugins.odatacliui.extensions.entityForAction
import com.jetbrains.rider.plugins.odatacliui.models.CliDialogModel
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.workspace.isProject
import com.jetbrains.rider.projectView.workspace.isWebReferenceFolder

class OpenCliDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val cliVersion = getCliVersion(project)
        val dialogModel = CliDialogModel(cliVersion)

        project.lifetime.launchOnUi {
            val dialog = CliDialog(dialogModel)
            dialog.show()
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val entity = e.entityForAction
        e.presentation.isVisible = entity.isWebReferenceFolder() || entity.isProject()
    }

    private fun getCliVersion(project: Project): String {
        return project.solution.protocolModel.cliVersion.valueOrNull
            ?: project.solution.protocolModel.getCliVersion.sync(Unit)
    }
}