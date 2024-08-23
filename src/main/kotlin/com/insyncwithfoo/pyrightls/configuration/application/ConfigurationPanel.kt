package com.insyncwithfoo.pyrightls.configuration.application

import com.insyncwithfoo.pyrightls.configuration.Hint
import com.insyncwithfoo.pyrightls.configuration.PathHintState
import com.insyncwithfoo.pyrightls.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyrightls.configuration.makeFlexible
import com.insyncwithfoo.pyrightls.configuration.reactiveLabel
import com.insyncwithfoo.pyrightls.configuration.triggerChange
import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNonNullableProperty
import com.intellij.ui.dsl.builder.toNullableProperty


private val relativePathHint: Hint
    get() = Hint.error(message("configurations.hint.globalMustBeAbsolute"))


private fun Row.makeAlwaysUseGlobalInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.alwaysUseGlobal.label")).apply(block)


private fun Row.makeGlobalExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    textFieldWithBrowseButton().makeFlexible().apply(block)


private fun Row.makeUseEditorFontInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.useEditorFont.label")).apply(block)


private fun Row.makeAddTooltipPrefixInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.addTooltipPrefix.label")).apply(block)


private fun Row.makeLinkErrorCodesInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.linkErrorCodes.label")).apply(block)


private fun Row.makeHoverSupportInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.hoverSupport.label")).apply(block)


private fun Row.makeCompletionSupportInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.completionSupport.label")).apply(block)


private fun Row.makeGoToDefinitionSupportInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.goToDefinitionSupport.label")).apply(block)


private fun Row.makeLogLevelInput(block: Cell<ComboBox<LogLevel>>.() -> Unit) = run {
    val renderer = SimpleListCellRenderer.create<LogLevel> { label, value, _ ->
        label.text = value.label
    }
    
    comboBox(LogLevel.entries, renderer).apply(block)
}


private fun Row.makeAutocompleteParenthesesInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autocompleteParentheses.label")).apply(block)


private fun Row.makeTaggedHintsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.taggedHints.label")).apply(block)


private fun Row.makeAutoImportCompletionsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoImportCompletions.label")).apply(block)


private fun Row.makeDiagnosticsSupportInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.diagnosticsSupport.label")).apply(block)


private fun Row.makeAutoRestartServerInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoRestartServer.label")).apply(block)


private fun Row.makeMonkeypatchAutoImportDetailsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.monkeypatchAutoImportDetails.label")).apply(block)


private fun Row.makeMonkeypatchTrailingQuoteBugInput(block: Cell<JBCheckBox>.() -> Unit) = run {
    val issueLink = HtmlChunk.link("https://youtrack.jetbrains.com/issue/IJPL-155741", " IJPL-155741")
    val label = message("configurations.monkeypatchTrailingQuoteBug.label")
    val comment = message("configurations.monkeypatchTrailingQuoteBug.comment", issueLink)
    
    checkBox(label).comment(comment).apply(block)
}


private fun Row.makeLocaleInput(block: Cell<ComboBox<Locale>>.() -> Unit) = run {
    val renderer = SimpleListCellRenderer.create<Locale> { label, value, _ ->
        label.text = value.toString()
    }
    
    comboBox(Locale.entries, renderer).apply(block)
}


@Suppress("DialogTitleCapitalization")
internal fun configurationPanel(state: Configurations) = panel {
    val executablePathHintState = PathHintState { path ->
        when {
            !path.isAbsolute -> relativePathHint
            else -> executablePathResolvingHint(path)
        }
    }
    
    row {
        makeAlwaysUseGlobalInput { bindSelected(state::alwaysUseGlobal) }
    }
    
    row(message("configurations.globalExecutable.label")) {
        makeGlobalExecutableInput {
            bindText(executablePathHintState.path)
            bindText(state::globalExecutable.toNonNullableProperty(""))
            triggerChange()
        }
    }
    row("") {
        reactiveLabel(executablePathHintState.hint)
    }
    
    group(message("configurations.group.tooltips")) {
        row {
            makeUseEditorFontInput { bindSelected(state::useEditorFont) }
        }
        row {
            makeAddTooltipPrefixInput { bindSelected(state::addTooltipPrefix) }
        }
        row {
            makeLinkErrorCodesInput { bindSelected(state::linkErrorCodes) }
        }
        row(message("configurations.locale.label")) {
            makeLocaleInput { bindItem(state::locale.toNullableProperty()) }
        }
    }
    
    group(message("configurations.group.languageServer")) {
        row {
            makeAutoRestartServerInput { bindSelected(state::autoRestartServer) }
        }
        
        separator()
        
        row {
            makeCompletionSupportInput { bindSelected(state::completionSupport) }
        }
        indent {
            row {
                makeAutoImportCompletionsInput { bindSelected(state::autoImportCompletions) }
            }
            indent {
                row {
                    makeMonkeypatchAutoImportDetailsInput { bindSelected(state::monkeypatchAutoImportDetails) }
                }
            }
            row {
                makeAutocompleteParenthesesInput { bindSelected(state::autocompleteParentheses) }
            }
            row {
                makeMonkeypatchTrailingQuoteBugInput { bindSelected(state::monkeypatchTrailingQuoteBug) }
            }
        }
        
        row {
            makeDiagnosticsSupportInput { bindSelected(state::diagnosticsSupport) }
        }
        indent {
            row {
                makeTaggedHintsInput { bindSelected(state::taggedHints) }
            }
        }
        
        row {
            makeHoverSupportInput { bindSelected(state::hoverSupport) }
        }
        row {
            makeGoToDefinitionSupportInput { bindSelected(state::goToDefinitionSupport) }
        }
        
        separator()
        
        row(message("configurations.logLevel.label")) {
            makeLogLevelInput { bindItem(state::logLevel.toNullableProperty()) }
        }
    }
    
}
