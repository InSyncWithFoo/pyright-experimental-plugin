package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.configuration.Hint
import com.insyncwithfoo.pyrightls.configuration.PathHintState
import com.insyncwithfoo.pyrightls.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyrightls.configuration.makeFlexible
import com.insyncwithfoo.pyrightls.configuration.reactiveLabel
import com.insyncwithfoo.pyrightls.configuration.triggerChange
import com.insyncwithfoo.pyrightls.message
import com.insyncwithfoo.pyrightls.path
import com.insyncwithfoo.pyrightls.resolvedAgainst
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNonNullableProperty
import com.intellij.ui.dsl.builder.toNullableProperty


private val unresolvablePathHint: Hint
    get() = Hint.error(message("configurations.hint.unresolvablePath"))


private fun Row.makeProjectExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    textFieldWithBrowseButton().makeFlexible().apply(block)


private fun Row.makeAutoSuggestExecutableInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoSuggestExecutable.label")).apply(block)


private fun Row.makeWorkspaceFoldersInput(block: Cell<ComboBox<WorkspaceFolders>>.() -> Unit) = run {
    val renderer = SimpleListCellRenderer.create<WorkspaceFolders> { label, value, _ ->
        label.text = value.label
    }
    
    comboBox(WorkspaceFolders.entries, renderer).apply(block)
}


private fun Row.makeTargetedFileExtensionsInput(block: Cell<ExpandableTextField>.() -> Unit) = run {
    val parser = DelimitedFileExtensionList::split
    val joiner = List<FileExtension>::join
    
    expandableTextField(parser, joiner).makeFlexible().apply(block)
}


private fun Row.makeDiagnosticModeInput(block: Cell<ComboBox<DiagnosticMode>>.() -> Unit) = run {
    val renderer = SimpleListCellRenderer.create<DiagnosticMode> { label, value, _ ->
        label.text = value.label
    }
    
    comboBox(DiagnosticMode.entries, renderer).apply(block)
}


private fun Row.makeAutoSearchPathsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoSearchPaths.label")).apply(block)


internal fun Configurable.configurationPanel(state: Configurations) = panel {
    val executablePathHintState = PathHintState { path ->
        when {
            project.path == null && !path.isAbsolute -> unresolvablePathHint
            else -> executablePathResolvingHint(path.resolvedAgainst(project.path))
        }
    }
    
    row {
        makeAutoSuggestExecutableInput { bindSelected(state::autoSuggestExecutable) }
    }
    
    row(message("configurations.projectExecutable.label")) {
        makeProjectExecutableInput {
            bindText(executablePathHintState.path)
            bindText(state::projectExecutable.toNonNullableProperty(""))
            triggerChange()
        }
    }
    row("") {
        reactiveLabel(executablePathHintState.hint)
    }
    
    @Suppress("DialogTitleCapitalization")
    group(message("configurations.group.languageServer")) {
        row {
            makeAutoSearchPathsInput { bindSelected(state::autoSearchPaths) }
        }
        row(message("configurations.targetedFileExtensions.label")) {
            makeTargetedFileExtensionsInput {
                bindText(
                    { state.targetedFileExtensions.orEmpty().deduplicate() },
                    { state.targetedFileExtensions = it.deduplicate() }
                )
            }
        }
        row(message("configurations.diagnosticMode.label")) {
            makeDiagnosticModeInput { bindItem(state::diagnosticMode.toNullableProperty()) }
        }
        row(message("configurations.workspaceFolders.label")) {
            makeWorkspaceFoldersInput { bindItem(state::workspaceFolders.toNullableProperty()) }
        }
    }
    
}
