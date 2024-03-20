package com.jetbrains.rider.plugins.odatacliui.terminal

import com.intellij.execution.configurations.GeneralCommandLine

class BatchCommandLine {
    val commands: ArrayList<GeneralCommandLine> = arrayListOf()

    fun add(command: GeneralCommandLine) = commands.add(command)

    fun getCommandLineString(): String {
        val sb = StringBuilder()
        for (i in commands.indices) {
            sb.appendLine("[$i]: ${commands[i].commandLineString}")
        }

        return sb.toString()
    }
}