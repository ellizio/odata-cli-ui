package com.jetbrains.rider.plugins.odatacliui.terminal

import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.jetbrains.rider.run.TerminalProcessHandler

final class BatchCommandLineExecutor(
    private val project: Project,
    private val batchCommandLine: BatchCommandLine,
    private val consoleView: ConsoleView) {

    final fun execute() {
        val listener = object : ProcessAdapter() {
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                consoleView.print(event.text, ConsoleViewContentType.getConsoleViewType(outputType))
            }
        }

        for (i in batchCommandLine.commands.indices) {
            val terminalHandler = TerminalProcessHandler(project, batchCommandLine.commands[i])
            terminalHandler.addProcessListener(listener)
            terminalHandler.startNotify()
            terminalHandler.waitFor()

            consoleView.print("\r\n\r\n\r\n", ConsoleViewContentType.LOG_INFO_OUTPUT)
        }
    }
}