package ru.ellizio.odatacliui.terminal.executors

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.jetbrains.rider.run.TerminalProcessHandler

class CommandLineExecutor(
    private val project: Project,
    private val commandLine: GeneralCommandLine,
    private val consoleView: ConsoleView) {

    private val newLine = System.lineSeparator() + System.lineSeparator();

    fun execute(): Boolean {
        var hasError = false;

        val listener = object : ProcessAdapter() {
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                val contentType = ConsoleViewContentType.getConsoleViewType(outputType)
                if (contentType == ConsoleViewContentType.ERROR_OUTPUT)
                    hasError = true;

                consoleView.print(event.text, contentType)
            }
        }

        val terminalHandler = TerminalProcessHandler(project, commandLine)
        terminalHandler.addProcessListener(listener)
        terminalHandler.startNotify()
        terminalHandler.waitFor()

        consoleView.print(newLine, ConsoleViewContentType.LOG_INFO_OUTPUT)

        return !hasError
    }
}