package ru.ellizio.odatacliui.models

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.jetbrains.rd.ide.model.DotnetToolDefinition
import com.jetbrains.rd.ide.model.protocolModel
import com.jetbrains.rider.model.dotNetActiveRuntimeModel
import ru.ellizio.odatacliui.Constants
import ru.ellizio.odatacliui.extensions.dotnetAddPackageCommand
import ru.ellizio.odatacliui.terminal.BatchCommandLine
import ru.ellizio.odatacliui.terminal.builders.BatchCommandLineBuilder
import com.jetbrains.rider.projectView.solution
import ru.ellizio.odatacliui.extensions.greaterOrEquals
import ru.ellizio.odatacliui.terminal.builders.CommandLineBuilder
import ru.ellizio.odatacliui.utils.DotnetToolsUtils
import kotlin.io.path.Path

private const val CONNECTED_SERVICES = "Connected Services"
private const val CSDL_NAME_LEGACY = "OData ServiceCsdl.xml"
private const val CSDL_NAME_SUFFIX = "Csdl.xml"

class CliDialogModel(project: Project, private val actionMetadata: ActionMetadata) {
    val odataCliTool: DotnetToolDefinition
    private val dotnetCliPath: String?

    private val atLeast031: Boolean

    init {
        odataCliTool = project.solution.protocolModel.getODataCliTool.sync(Unit)
        dotnetCliPath = project.solution.dotNetActiveRuntimeModel.activeRuntime.valueOrNull?.dotNetCliExePath

        atLeast031 = odataCliTool.version?.greaterOrEquals(0, 3, 1) ?: false
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

    fun getCsdlPath(): String {
        val csdl = if (atLeast031) "${serviceName.get()}$CSDL_NAME_SUFFIX" else CSDL_NAME_LEGACY
        return Path(CONNECTED_SERVICES, serviceName.get(), csdl).toString()
    }

    fun buildODataCliCommand(): GeneralCommandLine = CommandLineBuilder(DotnetToolsUtils.getToolDefaultPath("odata-cli"), "generate")
        .withParameter("--metadata-uri", metadataUri.get())
        .withParameter("--service-name", serviceName.get(), atLeast031)
        .withParameter("--file-name", fileName.get())
        .withParameter("--custom-headers", customHeaders.get())
        .withParameter("--proxy", proxy.get())
        .withParameter("--namespace-prefix", namespacePrefix.get())
        .withParameter("--excluded-operation-imports", excludedOperationImports.get())
        .withParameter("--excluded-bound-operations", excludedBoundOperations.get())
        .withParameter("--excluded-schema-types", excludedSchemaTypes.get())
        .withFlag("--upper-camel-case", upperCamelCase.get())
        .withFlag("--internal", internal.get())
        .withFlag("--multiple-files", multipleFiles.get())
        .withFlag("--ignore-unexpected-elements", ignoreUnexpectedElements.get())
        .withParameter("--outputdir", getOutputDirectory())
        .build()

    fun buildNuGetCommand(): BatchCommandLine = BatchCommandLineBuilder()
        .dotnetAddPackageCommand(dotnetCliPath, actionMetadata.projectPath, Constants.MICROSOFT_ODATA_CLIENT_PACKAGE_ID)
        .dotnetAddPackageCommand(dotnetCliPath, actionMetadata.projectPath, Constants.MICROSOFT_ODATA_CORE_PACKAGE_ID)
        .dotnetAddPackageCommand(dotnetCliPath, actionMetadata.projectPath, Constants.MICROSOFT_ODATA_EDM_PACKAGE_ID)
        .dotnetAddPackageCommand(dotnetCliPath, actionMetadata.projectPath, Constants.MICROSOFT_SPATIAL_PACKAGE_ID)
        .build()
}