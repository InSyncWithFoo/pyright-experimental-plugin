package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.configuration.Hint
import com.insyncwithfoo.pyrightls.configuration.asSecondColumnInput
import com.insyncwithfoo.pyrightls.configuration.bindText
import com.insyncwithfoo.pyrightls.configuration.displayPathHint
import com.insyncwithfoo.pyrightls.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyrightls.configuration.onInput
import com.insyncwithfoo.pyrightls.configuration.prefilledWithRandomPlaceholder
import com.insyncwithfoo.pyrightls.configuration.secondColumnPathInput
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


private fun unresolvablePathHint() =
    Hint.error(message("configurations.hint.unresolvablePath"))


private fun Row.makeProjectExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Row.makeAutoSuggestExecutableInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoSuggestExecutable.label")).apply(block)


private fun Row.makeWorkspaceFoldersInput(block: Cell<ComboBox<WorkspaceFolders>>.() -> Unit) = run {
    val renderer = SimpleListCellRenderer.create<WorkspaceFolders> { label, value, _ ->
        label.text = value.label
    }
    
    comboBox(WorkspaceFolders.entries, renderer).apply(block)
}


private fun Row.makeFileExtensionsInput(block: Cell<ExpandableTextField>.() -> Unit) {
    val parser = DelimitedFileExtensions::split
    val joiner = TargetedFileExtensions::join
    
    expandableTextField(parser, joiner).asSecondColumnInput().apply(block)
}


internal fun Configurable.configurationPanel(state: Configurations) = panel {
    // FIXME: The onInput() callbacks are too deeply nested.
    
    row {
        makeAutoSuggestExecutableInput { bindSelected(state::autoSuggestExecutable) }
    }
    
    row(message("configurations.projectExecutable.label")) {
        makeProjectExecutableInput {
            onInput(::displayPathHint) { path ->
                when {
                    project.path == null && !path.isAbsolute -> unresolvablePathHint()
                    else -> executablePathResolvingHint(path.resolvedAgainst(project.path))
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::projectExecutable)
        }
    }
    
    @Suppress("DialogTitleCapitalization")
    group(message("configurations.group.languageServer")) {
        row(message("configurations.targetedFileExtensions.label")) {
            makeFileExtensionsInput {
                comment(message("configurations.targetedFileExtensions.comment"))
                bindText(state::targetedFileExtensions.toNonNullableProperty(defaultValue = ""))
            }
        }
        row(message("configurations.workspaceFolders.label")) {
            makeWorkspaceFoldersInput { bindItem(state::workspaceFolders.toNullableProperty()) }
        }
    }
    
}
