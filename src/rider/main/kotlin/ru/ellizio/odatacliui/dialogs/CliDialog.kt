package ru.ellizio.odatacliui.dialogs

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.dsl.builder.*
import ru.ellizio.odatacliui.Constants
import ru.ellizio.odatacliui.UiBundle
import ru.ellizio.odatacliui.extensions.emptyText
import ru.ellizio.odatacliui.extensions.humanize
import ru.ellizio.odatacliui.models.CliDialogModel
import ru.ellizio.odatacliui.models.validators.CliDialogValidators
import javax.swing.JComponent

class CliDialog(private val model: CliDialogModel) : BaseDialog(false) {
    private lateinit var generationTabPanel: DialogPanel
    private lateinit var requestTabPanel: DialogPanel

    init {
        title = Constants.PLUGIN_NAME
        setOKActionEnabled(true)
        init()
    }

    override fun applyFields() {
        generationTabPanel.apply()
        requestTabPanel.apply()
        super.applyFields()
    }

    override fun setOKActionEnabled(isEnabled: Boolean) {
        if (!model.odataCliTool.installed) {
            setOKButtonTooltip(UiBundle.text("cli.ok-action-button.tooltip.not-installed"))
            super.setOKActionEnabled(false)
            return
        }
        else {
            setOKButtonTooltip(null)
        }

        super.setOKActionEnabled(isEnabled)
    }

    override fun createCenterPanel(): JComponent {
        val tabbedPane = JBTabbedPane()
        val generationTab = buildGenerationArgumentsTab()
        val requestTab = buildRequestArgumentsTab()
        tabbedPane.addTab(UiBundle.text("cli.tab.generation"), generationTab)
        tabbedPane.addTab(UiBundle.text("cli.tab.request"), requestTab)

        val version = if (!model.odataCliTool.installed) UiBundle.text("cli.cli-version.label-value.not-installed")
            else "${UiBundle.text("cli.cli-version.label-value.global")}, ${model.odataCliTool.version?.humanize()}"

        return panel {
            row {
                label(version)
                    .label(UiBundle.text("cli.cli-version.label"))
                    .comment(UiBundle.text("cli.cli-version.comment"))
            }.bottomGap(BottomGap.SMALL)
            row(UiBundle.text("cli.service-name.row")) {
                textField()
                    .align(AlignX.FILL)
                    .bindText(model.serviceName)
                    .validationOnInput(CliDialogValidators.serviceNameValidator())
                    .validationOnApply(CliDialogValidators.serviceNameValidator())
            }
            row(UiBundle.text("cli.metadata-source.row")) {
                textFieldWithBrowseButton(fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml"))
                    .align(AlignX.FILL)
                    .comment(UiBundle.text("cli.metadata-source.comment"), Int.MAX_VALUE)
                    .bindText(model.metadataUri)
                    .validationOnInput(CliDialogValidators.metadataUriValidator())
                    .validationOnApply(CliDialogValidators.metadataUriValidator())
            }.bottomGap(BottomGap.SMALL)
            row {
                cell(tabbedPane)
            }.resizableRow()
        }.apply {
            registerPanelValidators(this)
        }
    }

    private fun buildGenerationArgumentsTab(): DialogPanel = panel {
        row("--file-name") {
            textField()
                .align(AlignX.FILL)
                .emptyText(UiBundle.text("cli.filename.empty-text"))
                .comment(UiBundle.text("cli.filename.comment"))
                .bindText(model.fileName)
        }
        row("--namespace-prefix") {
            textField()
                .align(AlignX.FILL)
                .comment(UiBundle.text("cli.namespace-prefix.comment"))
                .bindText(model.namespacePrefix)
                .validationOnInput(CliDialogValidators.namespacePrefixValidator())
                .validationOnApply(CliDialogValidators.namespacePrefixValidator())
        }
        row("--excluded-operation-imports") {
            textField()
                .align(AlignX.FILL)
                .emptyText(UiBundle.text("cli.excluded-operation-imports.empty-text"))
                .comment(UiBundle.text("cli.excluded-operation-imports.comment"))
                .bindText(model.excludedOperationImports)
        }
        row("--excluded-bound-operations") {
            textField()
                .align(AlignX.FILL)
                .emptyText(UiBundle.text("cli.excluded-bound-operations.empty-text"))
                .comment(UiBundle.text("cli.excluded-bound-operations.comment"))
                .bindText(model.excludedBoundOperations)
        }
        row("--excluded-schema-types") {
            textField()
                .align(AlignX.FILL)
                .emptyText(UiBundle.text("cli.excluded-schema-types.empty-text"))
                .comment(UiBundle.text("cli.excluded-schema-types.comment"))
                .bindText(model.excludedSchemaTypes)
        }
        row {
            checkBox("--enable-internal")
                .align(AlignX.FILL)
                .comment(UiBundle.text("cli.enable-internal.comment"), Int.MAX_VALUE)
                .bindSelected(model.enableInternal)
        }
        row {
            checkBox("--omit-versioning-info")
                .align(AlignX.FILL)
                .comment(UiBundle.text("cli.omit-versioning-info.comment"), Int.MAX_VALUE)
                .bindSelected(model.omitVersioningInfo)
        }
        row {
            checkBox("--multiple-files")
                .align(AlignX.FILL)
                .comment(UiBundle.text("cli.multiple-files.comment"), Int.MAX_VALUE)
                .bindSelected(model.multipleFiles)
        }
        row {
            checkBox("--ignore-unexpected-elements")
                .align(AlignX.FILL)
                .comment(UiBundle.text("cli.ignore-unexpected-elements.comment"), Int.MAX_VALUE)
                .bindSelected(model.ignoreUnexpectedElements)
        }
        row {
            checkBox("--enable-tracking")
                .align(AlignX.FILL)
                .comment(UiBundle.text("cli.enable-tracking.comment"), Int.MAX_VALUE)
                .bindSelected(model.enableTracking)
        }
        row {
            checkBox("--upper-camel-case")
                .align(AlignX.FILL)
                .comment(UiBundle.text("cli.upper-camel-case.comment"), Int.MAX_VALUE)
                .bindSelected(model.upperCamelCase)
        }
    }.apply {
        registerPanelValidators(this)
        generationTabPanel = this
    }

    private fun buildRequestArgumentsTab(): DialogPanel = panel {
        row("--custom-headers") {
            textField()
                .align(AlignX.FILL)
                .emptyText(UiBundle.text("cli.custom-headers.empty-text"))
                .comment(UiBundle.text("cli.custom-headers.comment"), Int.MAX_VALUE)
                .bindText(model.customHeaders)
        }
        row("--proxy") {
            textField()
                .align(AlignX.FILL)
                .emptyText(UiBundle.text("cli.proxy.empty-text"))
                .comment(UiBundle.text("cli.proxy.comment"))
                .bindText(model.proxy)
                .validationOnInput(CliDialogValidators.proxyValidator())
                .validationOnApply(CliDialogValidators.proxyValidator())
        }
    }.apply {
        registerPanelValidators(this)
        requestTabPanel = this
    }
}