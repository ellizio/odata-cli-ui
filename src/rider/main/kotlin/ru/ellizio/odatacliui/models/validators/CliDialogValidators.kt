package ru.ellizio.odatacliui.models.validators

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.ValidationInfoBuilder
import javax.swing.JTextField

object CliDialogValidators {
    private val serviceNameRegex = Regex("^[0-9a-zA-Z_\\-. ]+\$")
    private val namespacePrefixRegex = Regex("^[a-zA-Z]\\w*(\\.[a-zA-Z]\\w*)*\$")
    private val proxyRegex = Regex("^(\\w+\\\\\\w+(:\\w+)?@)?\\w+:\\d+\$")

    fun serviceNameValidator(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? = {
        if (it.text.isBlank())
            error("Service name must not be empty")
        else if (it.text.startsWith(' '))
            error("Service name must not start with a space")
        else if (it.text.endsWith(' '))
            error("Service name must not end with a space")
        else if (!serviceNameRegex.matches(it.text))
            error("Service name must be in a valid format")
        else
            null
    }

    fun metadataUriValidator(): ValidationInfoBuilder.(TextFieldWithBrowseButton) -> ValidationInfo? = {
        if (it.text.isBlank())
            error("Metadata source must not be empty")
        else
            null
    }

    fun fileNameValidator(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? = {
        if (it.text.isNotEmpty() && it.text.isBlank())
            error("File name must not be entirely whitespace")
        else
            null
    }

    fun namespacePrefixValidator(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? = {
        if (it.text.isNotEmpty() && !namespacePrefixRegex.matches(it.text))
            error("Namespace prefix must be in a valid format")
        else
            null
    }

    fun proxyValidator(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? = {
        if (it.text.isNotEmpty() && !proxyRegex.matches(it.text))
            error("Proxy must be in a valid format")
        else
            null
    }
}