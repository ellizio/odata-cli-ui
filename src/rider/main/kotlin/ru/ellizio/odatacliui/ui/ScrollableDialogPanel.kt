package ru.ellizio.odatacliui.ui

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBScrollPane

class ScrollableDialogPanel(private val view: DialogPanel) : JBScrollPane(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER) {
    val panel: DialogPanel
        get() = view
}