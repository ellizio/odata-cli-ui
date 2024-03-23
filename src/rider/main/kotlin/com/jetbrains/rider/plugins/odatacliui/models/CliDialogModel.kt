package com.jetbrains.rider.plugins.odatacliui.models

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.jetbrains.rd.ide.model.protocolModel
import com.jetbrains.rider.model.RdCustomLocation
import com.jetbrains.rider.model.RdDependencyFolderDescriptor
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.plugins.odatacliui.Constants
import com.jetbrains.rider.plugins.odatacliui.extensions.dotnetAddPackageCommand
import com.jetbrains.rider.plugins.odatacliui.extensions.entityForAction
import com.jetbrains.rider.plugins.odatacliui.models.validators.CliDialogModelValidator
import com.jetbrains.rider.plugins.odatacliui.terminal.BatchCommandLine
import com.jetbrains.rider.plugins.odatacliui.terminal.BatchCommandLineBuilder
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity
import kotlin.io.path.Path

private const val CONNECTED_SERVICES = "Connected Services"

class CliDialogModel(event: AnActionEvent) {
    private val connectedServicesDir: String
    private val projectName: String
    private lateinit var projectPath: String

    val validator = CliDialogModelValidator()

    val cliVersion: String

    init {
        connectedServicesDir = getConnectedServicesDir(event)
        projectName = getProjectName(event)
        cliVersion = getCliVersion(event.project!!)
    }

    val serviceName = MutableProperty("")
    val metadataUri = MutableProperty("")

    val fileName = MutableProperty("")
    val namespacePrefix = MutableProperty("")
    val excludedOperationImports = MutableProperty("")
    val excludedBoundOperations = MutableProperty("")
    val excludedSchemaTypes = MutableProperty("")
    val internal = MutableProperty(false)
    val multipleFiles = MutableProperty(false)
    val ignoreUnexpectedElements = MutableProperty(false)
    val upperCamelCase = MutableProperty(true)

    val customHeaders = MutableProperty("")
    val proxy = MutableProperty("")

    private fun getCliVersion(project: Project): String {
        return project.solution.protocolModel.cliVersion.valueOrNull
            ?: project.solution.protocolModel.getCliVersion.sync(Unit)
    }

    private fun getConnectedServicesDir(action: AnActionEvent): String {
        var projectPath = ""

        val descriptor = prepareEntity(action)?.descriptor ?: return ""
        if (descriptor.location is RdCustomLocation) {
            val location = descriptor.location as RdCustomLocation
            projectPath = location.customLocation
        }

        this.projectPath = projectPath
        return Path(Path(projectPath).parent.toString(), CONNECTED_SERVICES).toString()
    }

    private fun getProjectName(action: AnActionEvent): String {
        val entity = prepareEntity(action)
        return entity?.name ?: ""
    }

    private fun prepareEntity(action: AnActionEvent): ProjectModelEntity? {
        val entity = action.entityForAction
        if (entity.descriptor is RdProjectDescriptor) {
            return entity
        }
        else if (entity.descriptor is RdDependencyFolderDescriptor) {
            return entity.parentEntity
        }

        return null
    }

    private fun getOutputDir() = Path(connectedServicesDir, serviceName.get()).toString()

    fun buildCommand(): BatchCommandLine = BatchCommandLineBuilder()
        .addCommand("odata-cli", "generate")
        .withNotBlankParameter("--metadata-uri", metadataUri.get())
        .withNotBlankParameter("--file-name", fileName.get())
        .withNotBlankParameter("--custom-headers", customHeaders.get())
        .withNotBlankParameter("--proxy", proxy.get())
        .withNotBlankParameter("--namespace-prefix", namespacePrefix.get())
        .withNotBlankParameter("--excluded-operation-imports", excludedOperationImports.get())
        .withNotBlankParameter("--excluded-bound-operations", excludedBoundOperations.get())
        .withNotBlankParameter("--excluded-schema-types", excludedSchemaTypes.get())
        .withFlag("--upper-camel-case", upperCamelCase.get())
        .withFlag("--internal", internal.get())
        .withFlag("--multiple-files", multipleFiles.get())
        .withFlag("--ignore-unexpected-elements", ignoreUnexpectedElements.get())
        .withNotBlankParameter("--outputdir", getOutputDir())
        .dotnetAddPackageCommand(projectPath, Constants.MICROSOFT_ODATA_CLIENT_PACKAGE_ID)
        .dotnetAddPackageCommand(projectPath, Constants.MICROSOFT_ODATA_CORE_PACKAGE_ID)
        .dotnetAddPackageCommand(projectPath, Constants.MICROSOFT_ODATA_EDM_PACKAGE_ID)
        .dotnetAddPackageCommand(projectPath, Constants.MICROSOFT_SPATIAL_PACKAGE_ID)
        .build()
}