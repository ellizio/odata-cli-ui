package com.jetbrains.rider.plugins.odatacliui.extensions

import com.jetbrains.rider.plugins.odatacliui.Constants
import com.jetbrains.rider.plugins.odatacliui.terminal.BatchCommandLineBuilder

fun BatchCommandLineBuilder.dotnetAddPackageCommand(csprojPath: String, packageId: String): BatchCommandLineBuilder {
    return addCommand("dotnet", "add")
        .withParameter(csprojPath)
        .withParameter("package")
        .withParameter(packageId)
        .withNotBlankParameter("-s", Constants.NUGET_SOURCE)
}