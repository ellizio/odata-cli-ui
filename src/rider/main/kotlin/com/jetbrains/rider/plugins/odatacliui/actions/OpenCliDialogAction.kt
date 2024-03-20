package com.jetbrains.rider.plugins.odatacliui.actions

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.rd.util.withBackgroundContext
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.jetbrains.rider.plugins.odatacliui.Constants
import com.jetbrains.rider.plugins.odatacliui.dialogs.CliDialog
import com.jetbrains.rider.plugins.odatacliui.extensions.entityForAction
import com.jetbrains.rider.plugins.odatacliui.models.CliDialogModel
import com.jetbrains.rider.plugins.odatacliui.terminal.BatchCommandLineExecutor
import com.jetbrains.rider.projectView.workspace.isProject
import com.jetbrains.rider.projectView.workspace.isWebReferenceFolder

class OpenCliDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val dialogModel = CliDialogModel(e)
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
        e.presentation.isVisible = entity.isWebReferenceFolder() || entity.isProject()
    }

    private suspend fun executeCommand(project: Project, model: CliDialogModel)
    {
        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        withUiContext {
            val toolWindow = ToolWindowManager.getInstance(project).registerToolWindow(Constants.PLUGIN_NAME) {
                anchor = ToolWindowAnchor.BOTTOM
                canCloseContent = true
            }
            val content = toolWindow.contentManager.factory.createContent(consoleView.component, "Generate", true)
            toolWindow.contentManager.addContent(content)
            toolWindow.activate {
                toolWindow.contentManager.setSelectedContent(content)
            }
        }

        val executor = BatchCommandLineExecutor(project, model.buildCommand(), consoleView)
        executor.execute()

        VirtualFileManager.getInstance().refreshWithoutFileWatcher(true)
    }
}