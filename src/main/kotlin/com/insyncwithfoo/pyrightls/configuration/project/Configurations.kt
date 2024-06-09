package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.components.BaseState


private const val FILE_EXTENSIONS_DELIMITER = "|"


internal typealias FileExtension = String
internal typealias DelimitedFileExtensionList = String


private fun FileExtension.normalize() = this.trim().lowercase()


private fun List<FileExtension>.toSetOfNormalized(): Set<FileExtension> =
    this.mapNotNullTo(mutableSetOf()) { extension ->
        extension.normalize().takeIf { it.isNotEmpty() }
    }


internal fun DelimitedFileExtensionList.split(): MutableList<FileExtension> =
    this.split(FILE_EXTENSIONS_DELIMITER).toSetOfNormalized().toMutableList()


internal fun List<FileExtension>.join() =
    this.toSetOfNormalized().joinToString(FILE_EXTENSIONS_DELIMITER)


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
    var targetedFileExtensions by string(listOf("py", "pyi").join())
}
