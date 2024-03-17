package com.jetbrains.rider.plugins.odatacliui.models

import com.jetbrains.rider.plugins.odatacliui.MutableProperty

class CliDialogModel(val cliVersion: String) {
    val serviceName = MutableProperty("")
    val metadataSource = MutableProperty("")

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
}