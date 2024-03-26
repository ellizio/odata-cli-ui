package ru.ellizio.odatacliui.models

import com.intellij.ui.dsl.builder.MutableProperty

class MutableProperty<T>(defaultValue: T) : MutableProperty<T> {
    private var internalValue = defaultValue

    override fun get(): T = internalValue

    override fun set(value: T) {
        internalValue = value
    }
}