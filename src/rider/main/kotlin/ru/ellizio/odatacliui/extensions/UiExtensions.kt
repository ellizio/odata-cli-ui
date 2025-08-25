package ru.ellizio.odatacliui.extensions

import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import ru.ellizio.odatacliui.ui.ScrollableDialogPanel

fun Cell<JBTextField>.emptyText(text: String): Cell<JBTextField> {
    component.emptyText.text = text
    return this
}

fun scrollablePanel(init: Panel.() -> Unit): ScrollableDialogPanel {
    val content = panel(init)
    return ScrollableDialogPanel(content).apply {
        border = null
        isOverlappingScrollBar = true
    }
}