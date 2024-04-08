package ru.ellizio.odatacliui.extensions

import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType

fun ConsoleView.printCommandError(t: Throwable, command: String) {
    print("Error attempting to call command ", ConsoleViewContentType.LOG_ERROR_OUTPUT)
    print(command, ConsoleViewContentType.LOG_INFO_OUTPUT)
    print(":${System.lineSeparator()}", ConsoleViewContentType.LOG_ERROR_OUTPUT)
    print(t.localizedMessage, ConsoleViewContentType.ERROR_OUTPUT)
}