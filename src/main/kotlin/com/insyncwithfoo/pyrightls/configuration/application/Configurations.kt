package com.insyncwithfoo.pyrightls.configuration.application

import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.components.BaseState


internal enum class LogLevel(val label: String) {
    ERROR(message("configurations.logLevel.error")),
    WARNING(message("configurations.logLevel.warning")),
    INFORMATION(message("configurations.logLevel.information")),
    TRACE(message("configurations.logLevel.trace"));
}


internal enum class Locale(private val value: String) {
    DEFAULT(message("configurations.locale.default")),
    CS("cs"),
    DE("de"),
    EN_US("en-us"),
    ES("es"),
    FR("fr"),
    IT("it"),
    JA("ja"),
    KO("ko"),
    PL("pl"),
    PT_BR("pt-br"),
    RU("ru"),
    TR("tr"),
    ZH_CN("zh-cn"),
    ZH_TW("zh-tw");
    
    override fun toString() = value
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
    var diagnosticsSupport by property(true)
    var autoRestartServer by property(false)
    var monkeypatchAutoImportDetails by property(true)
    var monkeypatchTrailingQuoteBug by property(true)
    var locale by enum(Locale.DEFAULT)
}
