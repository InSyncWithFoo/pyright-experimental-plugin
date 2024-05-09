package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.components.BaseState


internal typealias FileExtension = String
internal typealias DelimitedFileExtensionList = String


private const val fileExtensionsDelimiter = "|"


private fun FileExtension.normalize() = this.trim().lowercase()


private fun List<FileExtension>.toSetOfNormalized(): Set<FileExtension> =
    this.mapTo(mutableSetOf()) { it.normalize() }


internal fun DelimitedFileExtensionList.split(): MutableList<FileExtension> =
    this.split(fileExtensionsDelimiter).toSetOfNormalized().toMutableList()


internal fun List<FileExtension>.join() =
    this.toSetOfNormalized().joinToString(fileExtensionsDelimiter)


internal fun DelimitedFileExtensionList.deduplicate() =
    this.split().join()


internal enum class WorkspaceFolders(val label: String) {
    PROJECT_BASE(message("configurations.workspaceFolders.projectBase")),
    SOURCE_ROOTS(message("configurations.workspaceFolders.sourceRoots"));
}


internal class Configurations : BaseState() {
    var projectExecutable by string(null)
    var autoSuggestExecutable by property(true)
    var workspaceFolders by enum(WorkspaceFolders.PROJECT_BASE)
    var targetedFileExtensions by string(listOf("py").join())
}
