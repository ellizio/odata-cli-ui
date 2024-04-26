package ru.ellizio.odatacliui.extensions

import com.jetbrains.rd.ide.model.DotnetToolVersionDefinition

fun DotnetToolVersionDefinition.humanize() : String = "$major.$minor.$patch"