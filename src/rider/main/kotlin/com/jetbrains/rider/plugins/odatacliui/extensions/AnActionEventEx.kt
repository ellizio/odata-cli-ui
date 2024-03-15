package com.jetbrains.rider.plugins.odatacliui.extensions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.ProjectEntityView
import com.jetbrains.rider.projectView.actions.getProjectElementForAction
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity

val AnActionEvent.entityForAction: ProjectModelEntity
    get() = (dataContext.getProjectElementForAction() as ProjectEntityView).entity