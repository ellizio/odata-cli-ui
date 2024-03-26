package ru.ellizio.odatacliui.terminal

import com.intellij.execution.configurations.GeneralCommandLine

class BatchCommandLine {
    val commands: ArrayList<GeneralCommandLine> = arrayListOf()

    fun add(command: GeneralCommandLine) = commands.add(command)
}