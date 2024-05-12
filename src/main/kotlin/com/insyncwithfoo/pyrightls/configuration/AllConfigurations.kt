package com.insyncwithfoo.pyrightls.configuration

import com.insyncwithfoo.pyrightls.configuration.application.LogLevel
import com.insyncwithfoo.pyrightls.configuration.project.DelimitedFileExtensionList
import com.insyncwithfoo.pyrightls.configuration.project.FileExtension
import com.insyncwithfoo.pyrightls.configuration.project.WorkspaceFolders
import com.insyncwithfoo.pyrightls.configuration.project.split
import org.jetbrains.annotations.SystemDependent
import com.insyncwithfoo.pyrightls.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyrightls.configuration.project.Configurations as ProjectConfigurations


internal infix fun ApplicationConfigurations.mergeWith(other: ProjectConfigurations) =
    AllConfigurations(
        alwaysUseGlobal = this.alwaysUseGlobal,
        globalExecutable = this.globalExecutable,
        useEditorFont = this.useEditorFont,
        addTooltipPrefix = this.addTooltipPrefix,
        linkErrorCodes = this.linkErrorCodes,
        hoverSupport = this.hoverSupport,
        completionSupport = this.completionSupport,
        goToDefinitionSupport = this.goToDefinitionSupport,
        logLevel = this.logLevel,
        taggedHints = this.taggedHints,
        autoImportCompletions = this.autoImportCompletions,
        autocompleteParentheses = this.autocompleteParentheses,
        diagnosticsSupport = this.diagnosticsSupport,
        autoRestartServer = this.autoRestartServer,
        
        projectExecutable = other.projectExecutable,
        autoSuggestExecutable = other.autoSuggestExecutable,
        workspaceFolders = other.workspaceFolders,
        targetedFileExtensions = other.targetedFileExtensions
    )


internal data class AllConfigurations(
    val alwaysUseGlobal: Boolean,
    val globalExecutable: @SystemDependent String?,
    val useEditorFont: Boolean,
    val addTooltipPrefix: Boolean,
    val linkErrorCodes: Boolean,
    val hoverSupport: Boolean,
    val completionSupport: Boolean,
    val goToDefinitionSupport: Boolean,
    val logLevel: LogLevel,
    val taggedHints: Boolean,
    val autoImportCompletions: Boolean,
    val autocompleteParentheses: Boolean,
    val diagnosticsSupport: Boolean,
    val autoRestartServer: Boolean,
    
    val projectExecutable: @SystemDependent String?,
    val autoSuggestExecutable: Boolean,
    val workspaceFolders: WorkspaceFolders,
    val targetedFileExtensions: DelimitedFileExtensionList?
) {
    
    val executable: @SystemDependent String?
        get() = when {
            alwaysUseGlobal -> globalExecutable
            else -> projectExecutable ?: globalExecutable
        }
    
    val targetedFileExtensionList: List<FileExtension>
        get() = targetedFileExtensions.orEmpty().split()
    
}
