package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.configuration.Hint
import com.insyncwithfoo.pyrightls.configuration.bindText
import com.insyncwithfoo.pyrightls.configuration.displayPathHint
import com.insyncwithfoo.pyrightls.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyrightls.configuration.onInput
import com.insyncwithfoo.pyrightls.configuration.prefilledWithRandomPlaceholder
import com.insyncwithfoo.pyrightls.configuration.secondColumnPathInput
import com.insyncwithfoo.pyrightls.message
import com.insyncwithfoo.pyrightls.path
import com.insyncwithfoo.pyrightls.resolvedAgainst
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel


private fun unresolvablePathHint() =
    Hint.error(message("configurations.hint.unresolvablePath"))


private fun Row.makeProjectExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Row.makeAutoSuggestExecutableInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.project.autoSuggestExecutable.label")).apply(block)


internal fun Configurable.configurationPanel(state: Configurations) = panel {
    // FIXME: The onInput() callbacks are too deeply nested.
    
    row {
        makeAutoSuggestExecutableInput { bindSelected(state::autoSuggestExecutable) }
    }
    
    row(message("configurations.project.projectExecutable.label")) {
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
    
}
