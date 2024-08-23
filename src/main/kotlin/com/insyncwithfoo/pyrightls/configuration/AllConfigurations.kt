package com.insyncwithfoo.pyrightls.configuration

import com.insyncwithfoo.pyrightls.configuration.application.LogLevel
import com.insyncwithfoo.pyrightls.configuration.application.Locale
import com.insyncwithfoo.pyrightls.configuration.project.DelimitedFileExtensionList
import com.insyncwithfoo.pyrightls.configuration.project.DiagnosticMode
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
        monkeypatchAutoImportDetails = this.monkeypatchAutoImportDetails,
        monkeypatchTrailingQuoteBug = this.monkeypatchTrailingQuoteBug,
        locale = this.locale,
        
        projectExecutable = other.projectExecutable,
        autoSuggestExecutable = other.autoSuggestExecutable,
        workspaceFolders = other.workspaceFolders,
        targetedFileExtensions = other.targetedFileExtensions,
        diagnosticMode = other.diagnosticMode,
        autoSearchPaths = other.autoSearchPaths
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
    val monkeypatchAutoImportDetails: Boolean,
    val monkeypatchTrailingQuoteBug: Boolean,
    val locale: Locale,
    
    val projectExecutable: @SystemDependent String?,
    val autoSuggestExecutable: Boolean,
    val workspaceFolders: WorkspaceFolders,
    val targetedFileExtensions: DelimitedFileExtensionList?,
    val diagnosticMode: DiagnosticMode,
    val autoSearchPaths: Boolean
) {
    
    val executable: @SystemDependent String?
        get() = when {
            alwaysUseGlobal -> globalExecutable
            else -> projectExecutable ?: globalExecutable
        }
    
    val targetedFileExtensionList: List<FileExtension>
        get() = targetedFileExtensions.orEmpty().split()
    
}
