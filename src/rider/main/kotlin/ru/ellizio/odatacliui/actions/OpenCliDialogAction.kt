package ru.ellizio.odatacliui.actions

import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.*
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rd.ide.model.EmbeddedResourceDefinition
import com.jetbrains.rd.ide.model.protocolModel
import ru.ellizio.odatacliui.dialogs.CliDialog
import ru.ellizio.odatacliui.extensions.entityForAction
import ru.ellizio.odatacliui.extensions.toMetadata
import ru.ellizio.odatacliui.models.CliDialogModel
import ru.ellizio.odatacliui.terminal.executors.BatchCommandLineExecutor
import ru.ellizio.odatacliui.toolwindows.CliToolWindowManager
import com.jetbrains.rider.projectView.actions.isProjectModelReady
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.workspace.isProject
import com.jetbrains.rider.projectView.workspace.isWebReferenceFolder
import ru.ellizio.odatacliui.extensions.printCommandError
import ru.ellizio.odatacliui.models.ActionMetadata
import ru.ellizio.odatacliui.terminal.executors.CommandLineExecutor

class OpenCliDialogAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val actionMetadata = event.toMetadata() ?: return

        val dialogModel = CliDialogModel(project, actionMetadata)

        // launchOnUi is available since 233.11799.241
        // RD-2023.3 has build number 233.11799.261
        @Suppress("MissingRecentApi")
        project.lifetime.launchOnUi {
            val dialog = CliDialog(dialogModel)
            if (dialog.showAndGet()) {
                withBackgroundContext {
                    executeCommand(project, actionMetadata, dialogModel)
                }
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val entity = event.entityForAction
        if (entity == null) {
            event.presentation.isVisible = false
            return
        }
        event.presentation.isEnabled = event.project?.isProjectModelReady() ?: false
        event.presentation.isVisible = entity.isWebReferenceFolder() || entity.isProject()
    }

    private suspend fun executeCommand(project: Project, metadata: ActionMetadata, model: CliDialogModel)
    {
        var consoleView: ConsoleView? = null
        withUiContext {
            consoleView = CliToolWindowManager.getInstance(project).instantiateConsole()
        }

        val odataCliCommand = model.buildODataCliCommand()
        try {
            val odataCliExecutor = CommandLineExecutor(project, odataCliCommand, consoleView!!)
            val success = odataCliExecutor.execute()
            if (!success)
                return
        }
        catch (t: Throwable) {
            consoleView!!.printCommandError(t, odataCliCommand.commandLineString)
            return
        }

        // launchOnUi is available since 233.11799.241
        // RD-2023.3 has build number 233.11799.261
        @Suppress("MissingRecentApi")
        project.lifetime.launchOnUi {
            project.solution.protocolModel.addEmbeddedResource.startSuspending(project.lifetime, EmbeddedResourceDefinition(metadata.projectName, model.getCsdlPath()))
        }

        val nugetExecutor = BatchCommandLineExecutor(project, model.buildNuGetCommand(), consoleView!!)
        nugetExecutor.execute()

        VirtualFileManager.getInstance().refreshWithoutFileWatcher(true)
    }
}