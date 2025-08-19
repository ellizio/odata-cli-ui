package ru.ellizio.odatacliui.terminal.executors

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.jetbrains.rider.run.TerminalProcessHandler
import ru.ellizio.odatacliui.terminal.BatchCommandLine

class BatchCommandLineExecutor(
    private val project: Project,
    private val batchCommandLine: BatchCommandLine,
    private val consoleView: ConsoleView) {

    private val newLine = System.lineSeparator() + System.lineSeparator()

    fun execute() {
        val listener = object : ProcessListener {
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                consoleView.print(event.text, ConsoleViewContentType.getConsoleViewType(outputType))
            }
        }

        for (command in batchCommandLine.commands) {
            val terminalHandler = TerminalProcessHandler(project, command)
            terminalHandler.addProcessListener(listener)
            terminalHandler.startNotify()
            terminalHandler.waitFor()

            consoleView.print(newLine, ConsoleViewContentType.LOG_INFO_OUTPUT)
        }
    }
}