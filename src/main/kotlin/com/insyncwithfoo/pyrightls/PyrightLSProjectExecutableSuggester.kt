package com.insyncwithfoo.pyrightls

import com.insyncwithfoo.pyrightls.configuration.ConfigurationService
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.nio.file.Path
import com.insyncwithfoo.pyrightls.configuration.project.Configurations as ProjectConfigurations


private val Project.interpreterIsLocal: Boolean
    get() = when {
        path == null -> false
        interpreterPath == null -> false
        else -> interpreterPath!!.startsWith(path!!)
    }


private fun Project.executableShouldBeSuggested(): Boolean {
    val configurations = pyrightLSConfigurations
    
    val suggestionIsEnabled = configurations.autoSuggestExecutable
    val noProjectExecutableGiven = configurations.projectExecutable == null
    val globalExcutableIsPreferred = configurations.alwaysUseGlobal
    
    return suggestionIsEnabled && noProjectExecutableGiven && !globalExcutableIsPreferred
}


private fun Project.changePyrightLSConfigurations(action: ProjectConfigurations.() -> Unit) {
    val configurationService = ConfigurationService.getInstance(this)
    val projectConfigurations = configurationService.projectService.state
    
    projectConfigurations.action()
}


private fun Project.setAsExecutable(executable: Path) {
    changePyrightLSConfigurations { projectExecutable = executable.toString() }
}


private fun Project.disableSuggester() {
    changePyrightLSConfigurations { autoSuggestExecutable = false }
}


internal class PyrightLSProjectExecutableSuggester : ProjectActivity {
    
    override suspend fun execute(project: Project) {
        if (project.run { isPyrightLSInspectionEnabled() && executableShouldBeSuggested() && interpreterIsLocal }) {
            suggest(project, project.findPyrightLSExecutable() ?: return)
        }
    }
    
    private fun suggest(project: Project, executable: Path) {
        val projectPath = project.path ?: return
        val executableRelativized = projectPath.relativize(executable)
        
        val notification = pyrightNotificationGroup().createNotification(
            title = message("notifications.suggestion.title"),
            content = message("notifications.suggestion.body", executableRelativized),
            NotificationType.INFORMATION
        )
        
        notification.runThenNotify(project) {
            prettify()
            addSimpleExpiringAction(message("notifications.suggestion.action.setAbsolute")) {
                project.setAsExecutable(executable)
            }
            addSimpleExpiringAction(message("notifications.suggestion.action.setRelative")) {
                project.setAsExecutable(executableRelativized)
            }
            addSimpleExpiringAction(message("notifications.error.action.disableSuggester")) {
                project.disableSuggester()
            }
        }
    }
    
}
