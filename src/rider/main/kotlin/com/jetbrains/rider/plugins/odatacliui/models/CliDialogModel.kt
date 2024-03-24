package com.jetbrains.rider.plugins.odatacliui.models

import com.intellij.openapi.project.Project
import com.jetbrains.rd.ide.model.CliToolDefinition
import com.jetbrains.rd.ide.model.protocolModel
import com.jetbrains.rider.plugins.odatacliui.Constants
import com.jetbrains.rider.plugins.odatacliui.extensions.dotnetAddPackageCommand
import com.jetbrains.rider.plugins.odatacliui.models.validators.CliDialogModelValidator
import com.jetbrains.rider.plugins.odatacliui.terminal.BatchCommandLine
import com.jetbrains.rider.plugins.odatacliui.terminal.BatchCommandLineBuilder
import com.jetbrains.rider.projectView.solution
import kotlin.io.path.Path

private const val CONNECTED_SERVICES = "Connected Services"

class CliDialogModel(project: Project, private val actionMetadata: ActionMetadata) {
    val validator = CliDialogModelValidator()

    val cliDefinition: CliToolDefinition

    init {
        cliDefinition = project.solution.protocolModel.getCliDefinition.sync(Unit)
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

    private fun getOutputDirectory(): String = Path(Path(actionMetadata.projectPath).parent.toString(), CONNECTED_SERVICES, serviceName.get()).toString()

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
        .withNotBlankParameter("--outputdir", getOutputDirectory())
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_ODATA_CLIENT_PACKAGE_ID)
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_ODATA_CORE_PACKAGE_ID)
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_ODATA_EDM_PACKAGE_ID)
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_SPATIAL_PACKAGE_ID)
        .build()
}