package ru.ellizio.odatacliui.terminal.builders

import com.intellij.execution.configurations.GeneralCommandLine
import ru.ellizio.odatacliui.terminal.BatchCommandLine

class BatchCommandLineBuilder {
    private var commandsCount = 0
    private val commandLine = BatchCommandLine()
    private var command: GeneralCommandLine? = null

    fun addCommand(toolName: String, commandName: String): BatchCommandLineBuilder {
        if (command != null) {
            commandLine.add(command!!)
        }

        commandsCount++
        command = initCommand()
        command!!.exePath = toolName
        command!!.addParameter(commandName)
        return this
    }

    fun withParameter(parameter: String): BatchCommandLineBuilder {
        if (command == null)
            throw IllegalStateException()

        command!!.addParameter(parameter)
        return this
    }

    fun withParameter(parameterName: String, parameterValue: String?): BatchCommandLineBuilder {
        if (command == null)
            throw IllegalStateException()

        if (parameterValue.isNullOrBlank())
            return this

        command!!.addParameters(parameterName, parameterValue)
        return this
    }

    fun build(): BatchCommandLine {
        if (commandLine.commands.size != commandsCount) {
            commandLine.add(command!!)
        }

        return commandLine
    }

    private fun initCommand(): GeneralCommandLine {
        val command = GeneralCommandLine()
        command.charset = Charsets.UTF_8
        return command
    }
}