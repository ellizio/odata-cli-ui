package com.jetbrains.rider.plugins.odatacliui

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class CliDialog : DialogWrapper(false) {
    init {
        title = "OData Cli UI"
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            groupRowsRange("Information") {
                row("OData Cli Version:") {
                    label("todo")
                        .comment("Not installed? Follow <a href='https://learn.microsoft.com/en-us/odata/odatacli/getting-started#install'>instruction</a>")
                }
            }
            groupRowsRange("Arguments") {
                row("--metadata-uri") {
                    textFieldWithBrowseButton(fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml"))
                        .align(AlignX.FILL)
                        .comment("The URI of the metadata document. The value must be set to a valid service document URI or a local file path")
                }
                row("--custom-headers") {
                    textField()
                        .align(AlignX.FILL)
                        .comment("Headers that will get sent along with the request when fetching the metadata document from the service. Format: Header1:HeaderValue, Header2:HeaderValue")
                }
                row("--proxy") {
                    textField()
                        .align(AlignX.FILL)
                        .comment("Proxy settings. Format: domain\\\\user:password@SERVER:PORT")
                }
                row("--file-name") {
                    textField()
                        .align(AlignX.FILL)
                        .comment("The name of the generated file. If not provided, then the default name 'Reference.cs' is used")
                }
                row("--namespace-prefix") {
                    textField()
                        .align(AlignX.FILL)
                        .comment("The namespace of the client code generated. Example: NorthWindService.Client or it could be a name related to the OData endpoint that you are generating code for")
                }
                row("--excluded-operation-imports") {
                    textField()
                        .align(AlignX.FILL)
                        .comment("Comma-separated list of the names of operation imports to exclude from the generated code. Example: ExcludedOperationImport1, ExcludedOperationImport2")
                }
                row("--excluded-bound-operations") {
                    textField()
                        .align(AlignX.FILL)
                        .comment("Comma-separated list of the names of bound operations to exclude from the generated code. Example: BoundOperation1, BoundOperation2")
                }
                row("--excluded-schema-types") {
                    textField()
                        .align(AlignX.FILL)
                        .comment("Comma-separated list of the names of entity types to exclude from the generated code. Example: EntityType1, EntityType2, EntityType3")
                }
                row {
                    checkBox("--upper-camel-case")
                        .align(AlignX.FILL)
                        .comment("Transforms names (class and property names) to upper camel-case so that to better conform with C# naming conventions")
                }
                row {
                    checkBox("--internal")
                        .align(AlignX.FILL)
                        .comment("Applies the internal class modifier on generated classes instead of public thereby making them invisible outside the assembly")
                }
                row {
                    checkBox("--multiple-files")
                        .align(AlignX.FILL)
                        .comment("Split the generated classes into separate files instead of generating all the code in a single file")
                }
                row {
                    checkBox("--ignore-unexpected-elements")
                        .align(AlignX.FILL)
                        .comment("This flag indicates whether to ignore unexpected elements and attributes in the metadata document and generate the client code if any")
                }
            }
        }
    }
}