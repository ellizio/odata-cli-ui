package com.jetbrains.rider.plugins.odatacliui.models.validators

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.ValidationInfoBuilder
import javax.swing.JTextField

class CliDialogModelValidator {
    fun serviceNameValidation(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? = {
        if (it.text.trim().isEmpty())
            error("Service name must not be empty")
        else
            null
    }

    fun metadataUriValidation(): ValidationInfoBuilder.(TextFieldWithBrowseButton) -> ValidationInfo? = {
        if (it.text.trim().isEmpty())
            error("Metadata source must not be empty")
        else
            null
    }

    fun namespacePrefixValidation(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? = {
        if (it.text.contains(' '))
            error("Namespace prefix must not contain whitespaces")
        else
            null
    }
}