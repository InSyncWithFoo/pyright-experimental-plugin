package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.components.BaseState


internal typealias DelimitedFileExtensions = String
internal typealias TargetedFileExtensions = List<String>


internal const val fileExtensionsDelimiter = "|"


internal fun DelimitedFileExtensions.split(): MutableList<String> {
    return this.split(fileExtensionsDelimiter).mapTo(mutableListOf()) { it.trim().lowercase() }
}


internal fun TargetedFileExtensions.join(): String {
    return this.joinToString(fileExtensionsDelimiter) { it.trim().lowercase() }
}


internal enum class WorkspaceFolders(val label: String) {
    PROJECT_BASE(message("configurations.workspaceFolders.projectBase")),
    SOURCE_ROOTS(message("configurations.workspaceFolders.sourceRoots"));
}


internal class Configurations : BaseState() {
    var projectExecutable by string(null)
    var autoSuggestExecutable by property(true)
    var workspaceFolders by enum(WorkspaceFolders.PROJECT_BASE)
    var targetedFileExtensions by string("py")
}
