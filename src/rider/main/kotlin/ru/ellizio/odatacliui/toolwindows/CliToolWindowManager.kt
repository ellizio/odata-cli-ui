package ru.ellizio.odatacliui.toolwindows

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import ru.ellizio.odatacliui.CliIcons
import ru.ellizio.odatacliui.Constants
import ru.ellizio.odatacliui.UiBundle

@Service(Service.Level.PROJECT)
class CliToolWindowManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<CliToolWindowManager>()
    }

    private val toolWindow by lazy {
        ToolWindowManager.getInstance(project).registerToolWindow(Constants.PLUGIN_NAME) {
            anchor = ToolWindowAnchor.BOTTOM
            canCloseContent = true
            icon = CliIcons.CliToolWindow
        }
    }

    fun instantiateConsole(): ConsoleView {
        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content = toolWindow.contentManager.factory.createContent(consoleView.component, UiBundle.text("cli.tab.generate"), true)
        content.setDisposer(consoleView);
        toolWindow.contentManager.addContent(content)
        toolWindow.activate {
            toolWindow.contentManager.setSelectedContent(content)
        }
        return consoleView
    }
}