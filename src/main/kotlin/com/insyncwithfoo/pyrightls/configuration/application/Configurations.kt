package com.insyncwithfoo.pyrightls.configuration.application

import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.components.BaseState


internal enum class LogLevel(val label: String) {
    ERROR(message("configurations.logLevel.error")),
    WARNING(message("configurations.logLevel.warning")),
    INFORMATION(message("configurations.logLevel.information")),
    TRACE(message("configurations.logLevel.trace"));
}


internal class Configurations : BaseState() {
    var alwaysUseGlobal by property(false)
    var globalExecutable by string(null)
    var useEditorFont by property(false)
    var addTooltipPrefix by property(false)
    var linkErrorCodes by property(false)
    
    var hoverSupport by property(true)
    var completionSupport by property(false)
    var goToDefinitionSupport by property(false)
    var logLevel by enum(LogLevel.INFORMATION)
    var taggedHints by property(true)
    var autoImportCompletions by property(true)
    var autocompleteParentheses by property(false)
}
