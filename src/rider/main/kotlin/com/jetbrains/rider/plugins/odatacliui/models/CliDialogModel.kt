package com.jetbrains.rider.plugins.odatacliui.models

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.jetbrains.rd.ide.model.protocolModel
import com.jetbrains.rider.model.RdCustomLocation
import com.jetbrains.rider.model.RdDependencyFolderDescriptor
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.plugins.odatacliui.extensions.entityForAction
import com.jetbrains.rider.plugins.odatacliui.models.validators.CliDialogModelValidator
import com.jetbrains.rider.plugins.odatacliui.terminal.CommandBuilder
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity
import kotlin.io.path.Path

class CliDialogModel(event: AnActionEvent) {
    private val connectedServicesDir: String

    val validator = CliDialogModelValidator()

    val cliVersion: String

    init {
        connectedServicesDir = getConnectedServicesDir(event.entityForAction)
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

    private fun getConnectedServicesDir(entity: ProjectModelEntity): String {
        var projectPath = ""

        val descriptor = entity.descriptor
        if (descriptor is RdProjectDescriptor && descriptor.location is RdCustomLocation) {
            val location = descriptor.location as RdCustomLocation
            projectPath = location.customLocation
        }
        else if (descriptor is RdDependencyFolderDescriptor && entity.parentEntity?.descriptor?.location is RdCustomLocation) {
            val location = entity.parentEntity!!.descriptor.location as RdCustomLocation
            projectPath = location.customLocation
        }

        return Path(Path(projectPath).parent.toString(), "Connected Services").toString()
    }

    private fun getOutputDir() = Path(connectedServicesDir, serviceName.get()).toString()

    fun buildCommand(): GeneralCommandLine = CommandBuilder("odata-cli", "generate")
        .addIfNotBlank("--metadata-uri", metadataUri.get())
        .addIfNotBlank("--file-name", fileName.get())
        .addIfNotBlank("--custom-headers", customHeaders.get())
        .addIfNotBlank("--proxy", proxy.get())
        .addIfNotBlank("--namespace-prefix", namespacePrefix.get())
        .addIfNotBlank("--excluded-operation-imports", excludedOperationImports.get())
        .addIfNotBlank("--excluded-bound-operations", excludedBoundOperations.get())
        .addIfNotBlank("--excluded-schema-types", excludedSchemaTypes.get())
        .addFlag("--upper-camel-case", upperCamelCase.get())
        .addFlag("--internal", internal.get())
        .addFlag("--multiple-files", multipleFiles.get())
        .addFlag("--ignore-unexpected-elements", ignoreUnexpectedElements.get())
        .addIfNotBlank("--outputdir", getOutputDir())
        .build()
}