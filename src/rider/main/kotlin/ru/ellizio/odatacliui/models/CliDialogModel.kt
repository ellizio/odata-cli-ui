package ru.ellizio.odatacliui.models

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.jetbrains.rd.ide.model.CliTool
import com.jetbrains.rd.ide.model.protocolModel
import ru.ellizio.odatacliui.Constants
import ru.ellizio.odatacliui.extensions.dotnetAddPackageCommand
import ru.ellizio.odatacliui.models.validators.CliDialogModelValidator
import ru.ellizio.odatacliui.terminal.BatchCommandLine
import ru.ellizio.odatacliui.terminal.builders.BatchCommandLineBuilder
import com.jetbrains.rider.projectView.solution
import ru.ellizio.odatacliui.terminal.builders.CommandLineBuilder
import kotlin.io.path.Path

private const val CONNECTED_SERVICES = "Connected Services"
private const val CSDL_NAME = "OData ServiceCsdl.xml"

class CliDialogModel(project: Project, private val actionMetadata: ActionMetadata) {
    val validator = CliDialogModelValidator()

    val odataCliTool: CliTool

    init {
        odataCliTool = project.solution.protocolModel.getODataCliTool.sync(Unit)
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

    fun getCsdlPath(): String = Path(CONNECTED_SERVICES, serviceName.get(), CSDL_NAME).toString()

    fun buildODataCliCommand(): GeneralCommandLine = CommandLineBuilder("odata-cli", "generate")
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
        .build()

    fun buildNuGetCommand(): BatchCommandLine = BatchCommandLineBuilder()
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_ODATA_CLIENT_PACKAGE_ID)
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_ODATA_CORE_PACKAGE_ID)
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_ODATA_EDM_PACKAGE_ID)
        .dotnetAddPackageCommand(actionMetadata.projectPath, Constants.MICROSOFT_SPATIAL_PACKAGE_ID)
        .build()
}