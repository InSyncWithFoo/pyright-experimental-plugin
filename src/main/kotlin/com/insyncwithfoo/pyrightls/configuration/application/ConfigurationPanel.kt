package com.insyncwithfoo.pyrightls.configuration.application

import com.insyncwithfoo.pyrightls.configuration.Hint
import com.insyncwithfoo.pyrightls.configuration.bindText
import com.insyncwithfoo.pyrightls.configuration.displayPathHint
import com.insyncwithfoo.pyrightls.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyrightls.configuration.onInput
import com.insyncwithfoo.pyrightls.configuration.prefilledWithRandomPlaceholder
import com.insyncwithfoo.pyrightls.configuration.secondColumnPathInput
import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNullableProperty


private fun relativePathHint() =
    Hint.error(message("configurations.hint.globalMustBeAbsolute"))


private fun Row.makeAlwaysUseGlobalInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.alwaysUseGlobal.label")).apply(block)


private fun Row.makeGlobalExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


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


@Suppress("DialogTitleCapitalization")
internal fun configurationPanel(state: Configurations) = panel {
    // FIXME: The onInput() callbacks are too deeply nested.
    
    row {
        makeAlwaysUseGlobalInput { bindSelected(state::alwaysUseGlobal) }
    }
    
    row(message("configurations.globalExecutable.label")) {
        makeGlobalExecutableInput {
            onInput(::displayPathHint) { path ->
                when {
                    !path.isAbsolute -> relativePathHint()
                    else -> executablePathResolvingHint(path)
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::globalExecutable)
        }
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
    }
    
    group(message("configurations.group.languageServer")) {
        buttonsGroup(message("configurations.group.languageServer.client")) {
            row {
                makeHoverSupportInput { bindSelected(state::hoverSupport) }
                makeCompletionSupportInput { bindSelected(state::completionSupport) }
                makeGoToDefinitionSupportInput { bindSelected(state::goToDefinitionSupport) }
            }
            row {
                makeAutocompleteParenthesesInput { bindSelected(state::autocompleteParentheses) }
            }
        }
        buttonsGroup(message("configurations.group.languageServer.server")) {
            row {
                makeTaggedHintsInput { bindSelected(state::taggedHints) }
            }
            row {
                makeAutoImportCompletionsInput { bindSelected(state::autoImportCompletions) }
            }
            row(message("configurations.logLevel.label")) {
                makeLogLevelInput { bindItem(state::logLevel.toNullableProperty()) }
            }
        }
    }
    
}
