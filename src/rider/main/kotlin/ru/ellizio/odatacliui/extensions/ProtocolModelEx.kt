package ru.ellizio.odatacliui.extensions

import com.jetbrains.rd.ide.model.DotnetToolVersionDefinition

fun DotnetToolVersionDefinition.humanize() : String = "$major.$minor.$patch"

fun DotnetToolVersionDefinition.greaterOrEquals(major: Int, minor: Int, patch: Int) : Boolean {
    if (this.major > major)
        return true

    if (this.major == major && this.minor > minor)
        return true

    if (this.major == major && this.minor == minor && this.patch >= patch)
        return true

    return false
}