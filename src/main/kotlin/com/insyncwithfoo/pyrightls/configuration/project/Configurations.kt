package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.components.BaseState


internal enum class WorkspaceFolders(val label: String) {
    PROJECT_BASE(message("configurations.project.workspaceFolders.projectBase")),
    SOURCE_ROOTS(message("configurations.project.workspaceFolders.sourceRoots"));
}


internal class Configurations : BaseState() {
    var projectExecutable by string(null)
    var autoSuggestExecutable by property(true)
    
    var workspaceFolders by enum(WorkspaceFolders.PROJECT_BASE)
}
