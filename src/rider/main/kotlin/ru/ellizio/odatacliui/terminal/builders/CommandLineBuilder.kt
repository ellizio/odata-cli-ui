package ru.ellizio.odatacliui.terminal.builders

import com.intellij.execution.configurations.GeneralCommandLine

class CommandLineBuilder(toolName: String, commandName: String) {
    private val command: GeneralCommandLine

    init {
        command = initCommand()
        command.exePath = toolName
        command.addParameter(commandName)
    }

    fun withParameter(parameterName: String, parameterValue: String?): CommandLineBuilder {
        if (parameterValue.isNullOrBlank())
            return this

        command.addParameters(parameterName, parameterValue)
        return this
    }

    fun withParameter(parameterName: String, parameterValue: String?, condition: Boolean): CommandLineBuilder {
        if (condition)
            return withParameter(parameterName, parameterValue)

        return this
    }

    fun withFlag(parameterName: String, parameterValue: Boolean): CommandLineBuilder {
        if (!parameterValue)
            return this

        command.addParameter(parameterName)
        return this
    }

    fun build(): GeneralCommandLine = command

    private fun initCommand(): GeneralCommandLine {
        val command = GeneralCommandLine()
        command.charset = Charsets.UTF_8
        return command
    }
}