package com.jetbrains.rider.plugins.odatacliui

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "UiBundle"

object UiBundle : DynamicBundle(BUNDLE) {
    fun text(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String = getMessage(key, *params)
}