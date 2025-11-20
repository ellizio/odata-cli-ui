package ru.ellizio.odatacliui

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "UiBundle"

object UiBundle {
    private val instance = DynamicBundle(UiBundle::class.java, BUNDLE)

    fun text(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String = instance.getMessage(key, *params)
}