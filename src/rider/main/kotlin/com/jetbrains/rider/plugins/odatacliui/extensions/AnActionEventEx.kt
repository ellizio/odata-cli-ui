package com.jetbrains.rider.plugins.odatacliui.extensions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.jetbrains.rider.model.RdCustomLocation
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.plugins.odatacliui.models.ActionMetadata
import com.jetbrains.rider.projectView.views.solutionExplorer.nodes.SolutionExplorerModelNode
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity
import com.jetbrains.rider.projectView.workspace.isProject
import com.jetbrains.rider.projectView.workspace.isWebReferenceFolder

val AnActionEvent.entityForAction: ProjectModelEntity?
    get() {
        val items = getData(PlatformCoreDataKeys.SELECTED_ITEMS)
        if (items == null || items.size > 1 || items[0] !is SolutionExplorerModelNode) {
            return null
        }
        return (items[0] as SolutionExplorerModelNode).entity
    }

fun AnActionEvent.toMetadata(): ActionMetadata? {
    val entity = entityForAction ?: return null
    val descriptor: RdProjectDescriptor

    if (entity.isProject()) {
        descriptor = entity.descriptor as RdProjectDescriptor
    }
    else if (entity.isWebReferenceFolder()) {
        descriptor = entity.parentEntity!!.descriptor as RdProjectDescriptor
    }
    else {
        throw IllegalStateException()
    }

    val projectPath = (descriptor.location as RdCustomLocation).customLocation

    return ActionMetadata(projectPath)
}