package com.jetbrains.rider.plugins.odatacliui.dialogs

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.dsl.builder.*
import com.jetbrains.rider.plugins.odatacliui.models.CliDialogModel
import javax.swing.JComponent

@Suppress("UnstableApiUsage")
class CliDialog(private val model: CliDialogModel) : DialogWrapper(false) {
    init {
        title = "OData Cli UI"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val tabbedPane = JBTabbedPane()
        val generationTab = buildGenerationArgumentsTab()
        val requestTab = buildRequestArgumentsTab()
        tabbedPane.addTab("Generation Arguments", generationTab)
        tabbedPane.addTab("Request Arguments", requestTab)

        return panel {
            row {
                label(model.cliVersion)
                    .label("OData Cli Version:")
                    .comment("Not installed? Follow <a href='https://learn.microsoft.com/en-us/odata/odatacli/getting-started#install'>instruction</a>")
            }.bottomGap(BottomGap.MEDIUM)
            row("Service name:") {
                textField()
                    .align(AlignX.FILL)
                    .bindText(model.serviceName)
            }
            row("Metadata source:") {
                textFieldWithBrowseButton(fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml"))
                    .align(AlignX.FILL)
                    .comment("The URI of the metadata document. The value must be set to a valid service document URI or a local file path", Int.MAX_VALUE)
                    .bindText(model.metadataUri)
            }.bottomGap(BottomGap.SMALL)
            row {
                cell(tabbedPane)
            }.resizableRow()
        }.apply {
            registerIntegratedPanel(generationTab)
            registerIntegratedPanel(requestTab)
        }
    }

    private fun buildGenerationArgumentsTab(): DialogPanel = panel {
        row("--file-name") {
            textField()
                .align(AlignX.FILL)
                .comment("The name of the generated file. If not provided, then the default name 'Reference.cs' is used", Int.MAX_VALUE)
                .bindText(model.fileName)
        }
        row("--namespace-prefix") {
            textField()
                .align(AlignX.FILL)
                .comment("The namespace of the client code generated. <b>Example: NorthWindService.Client</b> or it could be a name related to the OData endpoint that you are generating code for", Int.MAX_VALUE)
                .bindText(model.namespacePrefix)
        }
        row("--excluded-operation-imports") {
            textField()
                .align(AlignX.FILL)
                .comment("Comma-separated list of the names of operation imports to exclude from the generated code. <b>Example: ExcludedOperationImport1, ExcludedOperationImport2</b>", Int.MAX_VALUE)
                .bindText(model.excludedOperationImports)
        }
        row("--excluded-bound-operations") {
            textField()
                .align(AlignX.FILL)
                .comment("Comma-separated list of the names of bound operations to exclude from the generated code. <b>Example: BoundOperation1, BoundOperation2</b>", Int.MAX_VALUE)
                .bindText(model.excludedBoundOperations)
        }
        row("--excluded-schema-types") {
            textField()
                .align(AlignX.FILL)
                .comment("Comma-separated list of the names of entity types to exclude from the generated code. <b>Example: EntityType1, EntityType2, EntityType3</b>", Int.MAX_VALUE)
                .bindText(model.excludedSchemaTypes)
        }
        row {
            checkBox("--internal")
                .align(AlignX.FILL)
                .comment("Applies the internal class modifier on generated classes instead of public thereby making them invisible outside the assembly", Int.MAX_VALUE)
                .bindSelected(model.internal)
        }
        row {
            checkBox("--multiple-files")
                .align(AlignX.FILL)
                .comment("Split the generated classes into separate files instead of generating all the code in a single file", Int.MAX_VALUE)
                .bindSelected(model.multipleFiles)
        }
        row {
            checkBox("--ignore-unexpected-elements")
                .align(AlignX.FILL)
                .comment("This flag indicates whether to ignore unexpected elements and attributes in the metadata document and generate the client code if any", Int.MAX_VALUE)
                .bindSelected(model.ignoreUnexpectedElements)
        }
        row {
            checkBox("--upper-camel-case")
                .align(AlignX.FILL)
                .comment("Transforms names (class and property names) to upper camel-case so that to better conform with C# naming conventions", Int.MAX_VALUE)
                .bindSelected(model.upperCamelCase)
        }
    }

    private fun buildRequestArgumentsTab(): DialogPanel = panel {
        row("--custom-headers") {
            textField()
                .align(AlignX.FILL)
                .comment("Headers that will get sent along with the request when fetching the metadata document from the service. <b>Format: Header1:HeaderValue, Header2:HeaderValue</b>", Int.MAX_VALUE)
                .bindText(model.customHeaders)
        }
        row("--proxy") {
            textField()
                .align(AlignX.FILL)
                .comment("Proxy settings. <b>Format: domain\\\\user:password@SERVER:PORT</b>", Int.MAX_VALUE)
                .bindText(model.proxy)
        }
    }
}