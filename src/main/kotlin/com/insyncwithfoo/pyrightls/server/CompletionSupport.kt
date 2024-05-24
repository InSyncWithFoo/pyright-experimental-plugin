package com.insyncwithfoo.pyrightls.server

import com.google.gson.JsonObject
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.customization.LspCompletionSupport
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import org.eclipse.lsp4j.InsertTextFormat


private const val CARET_POSITION = "\$0"


private val String.isSingleQuoted: Boolean
    get() = this.startsWith("'") && this.endsWith("'")


private val String.isDoubleQuoted: Boolean
    get() = this.startsWith("\"") && this.endsWith("\"")


private val CompletionItem.isCallable: Boolean
    get() = kind in listOf(
        CompletionItemKind.Method,
        CompletionItemKind.Function,
        CompletionItemKind.Constructor
    )


private val CompletionItem.isAutoImportCompletion: Boolean
    get() {
        val data = this.data
        return data is JsonObject && data.has("autoImportText")
    }


private val CompletionItem.isQuoted: Boolean
    get() = label.isSingleQuoted || label.isDoubleQuoted


private val CompletionItem.quote: Char
    get() = label[0]


private val CompletionParameters.nextCharacter: Char
    get() = editor.document.charsSequence[offset]


private fun CompletionItem.completeWithParentheses() {
    insertText = "$label($CARET_POSITION)"
    insertTextFormat = InsertTextFormat.Snippet
}


private fun CompletionItem.useSourceAsDetailIfPossible() {
    // https://github.com/microsoft/pyright/blob/0b7860b/packages/pyright-internal/src/languageService/completionProvider.ts#L932-L934
    detail = labelDetails?.description ?: ""
}


private fun CompletionItem.removeTrailingQuote() {
    insertText = label.dropLast(1)
}


@Suppress("UnstableApiUsage")
internal class CompletionSupport(project: Project) : LspCompletionSupport() {
    
    private val configurations = project.pyrightLSConfigurations
    
    override fun createLookupElement(parameters: CompletionParameters, item: CompletionItem): LookupElement? {
        if (item.isCallable && configurations.autocompleteParentheses) {
            item.completeWithParentheses()
        }
        
        if (item.isAutoImportCompletion) {
            item.useSourceAsDetailIfPossible()
        }
        
        if (item.isQuoted && parameters.nextCharacter == item.quote) {
            item.removeTrailingQuote()
        }
        
        return super.createLookupElement(parameters, item)
    }
    
}
