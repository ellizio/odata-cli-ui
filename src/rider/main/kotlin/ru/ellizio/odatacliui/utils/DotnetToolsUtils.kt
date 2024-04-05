package ru.ellizio.odatacliui.utils

import com.intellij.openapi.util.SystemInfo
import kotlin.io.path.Path

object DotnetToolsUtils {
    fun getToolDefaultPath(toolName: String): String {
        val homePath = System.getenv(if (SystemInfo.isWindows) "USERPROFILE" else "HOME")
        return Path(homePath, ".dotnet", "tools", toolName).toString()
    }
}