package com.jetbrains.rider.plugins.odatacliui.terminal

import com.intellij.execution.configurations.GeneralCommandLine

class CommandBuilder(toolName: String, commandName: String) {
    private val command = GeneralCommandLine()

    init {
        command.exePath = toolName
        command.addParameter(commandName)
    }

    fun addIfNotBlank(parameterName: String, parameterValue: String?): CommandBuilder {
        if (parameterValue.isNullOrBlank()) {
            return this
        }

        command.addParameters(parameterName, parameterValue)
        return this
    }

    fun addFlag(parameterName: String, parameterValue: Boolean): CommandBuilder {
        if (!parameterValue) {
            return this
        }

        command.addParameters(parameterName)
        return this
    }

    fun build(): GeneralCommandLine = command
}