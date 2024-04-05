package ru.ellizio.odatacliui.extensions

import ru.ellizio.odatacliui.Constants
import ru.ellizio.odatacliui.terminal.builders.BatchCommandLineBuilder

fun BatchCommandLineBuilder.dotnetAddPackageCommand(dotnetExePath: String?, csprojPath: String, packageId: String): BatchCommandLineBuilder {
    return addCommand(dotnetExePath ?: "dotnet", "add")
        .withParameter(csprojPath)
        .withParameter("package")
        .withParameter(packageId)
        .withParameter("-s", Constants.NUGET_SOURCE)
}