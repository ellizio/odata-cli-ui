package com.jetbrains.rider.plugins.odatacliui.extensions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.jetbrains.rider.projectView.views.solutionExplorer.nodes.SolutionExplorerModelNode
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity

val AnActionEvent.entityForAction: ProjectModelEntity?
    get() {
        val items = getData(PlatformCoreDataKeys.SELECTED_ITEMS)
        if (items == null || items.size > 1 || items[0] !is SolutionExplorerModelNode) {
            return null
        }
        return (items[0] as SolutionExplorerModelNode).entity
    }