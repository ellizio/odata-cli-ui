package com.jetbrains.rider.plugins.odatacliui.extensions

import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell

fun Cell<JBTextField>.emptyText(text: String): Cell<JBTextField> {
    this.component.emptyText.text = text
    return this
}