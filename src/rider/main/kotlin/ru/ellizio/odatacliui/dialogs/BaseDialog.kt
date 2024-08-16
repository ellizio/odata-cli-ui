package ru.ellizio.odatacliui.dialogs

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import java.awt.Container

abstract class BaseDialog(canBeParent: Boolean) : DialogWrapper(canBeParent) {
    private val panelsStates: MutableMap<Container, Boolean> = mutableMapOf()

    protected fun registerPanelValidators(panel: DialogPanel) {
        if (panelsStates.put(panel, true) != null)
            throw Exception("Panel validators are already registered")

        panel.registerValidators(disposable) { validations ->
            panelsStates[panel] = validations.all { it.value.okEnabled }
            isOKActionEnabled = panelsStates.all { it.value }
        }
    }
}