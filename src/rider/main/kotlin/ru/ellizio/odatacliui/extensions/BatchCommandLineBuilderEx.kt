package ru.ellizio.odatacliui.extensions

import ru.ellizio.odatacliui.Constants
import ru.ellizio.odatacliui.terminal.BatchCommandLineBuilder

fun BatchCommandLineBuilder.dotnetAddPackageCommand(csprojPath: String, packageId: String): BatchCommandLineBuilder {
    return addCommand("dotnet", "add")
        .withParameter(csprojPath)
        .withParameter("package")
        .withParameter(packageId)
        .withNotBlankParameter("-s", Constants.NUGET_SOURCE)
}