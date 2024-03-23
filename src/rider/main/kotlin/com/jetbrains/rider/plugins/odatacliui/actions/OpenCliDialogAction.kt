package com.jetbrains.rider.plugins.odatacliui.actions

import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.*
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rider.plugins.odatacliui.dialogs.CliDialog
import com.jetbrains.rider.plugins.odatacliui.extensions.entityForAction
import com.jetbrains.rider.plugins.odatacliui.extensions.toMetadata
import com.jetbrains.rider.plugins.odatacliui.models.CliDialogModel
import com.jetbrains.rider.plugins.odatacliui.terminal.BatchCommandLineExecutor
import com.jetbrains.rider.plugins.odatacliui.toolwindows.CliToolWindowManager
import com.jetbrains.rider.projectView.actions.isProjectModelReady
import com.jetbrains.rider.projectView.workspace.isProject
import com.jetbrains.rider.projectView.workspace.isWebReferenceFolder

class OpenCliDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val actionMetadata = e.toMetadata() ?: return

        val dialogModel = CliDialogModel(project, actionMetadata)
        project.lifetime.launchOnUi {
            val dialog = CliDialog(dialogModel)
            if (dialog.showAndGet()) {
                withBackgroundContext {
                    executeCommand(project, dialogModel)
                }
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val entity = e.entityForAction
        if (entity == null) {
            e.presentation.isVisible = false
            return
        }
        e.presentation.isEnabled = e.project?.isProjectModelReady() ?: false
        e.presentation.isVisible = entity.isWebReferenceFolder() || entity.isProject()
    }

    private suspend fun executeCommand(project: Project, model: CliDialogModel)
    {
        var consoleView: ConsoleView? = null
        withUiContext {
            consoleView = CliToolWindowManager.getInstance(project).instantiateConsole()
        }

        val executor = BatchCommandLineExecutor(project, model.buildCommand(), consoleView!!)
        executor.execute()

        VirtualFileManager.getInstance().refreshWithoutFileWatcher(true)
    }
}