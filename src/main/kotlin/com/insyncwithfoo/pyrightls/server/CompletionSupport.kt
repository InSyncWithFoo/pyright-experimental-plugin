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


private fun CompletionItem.completeWithParentheses() {
    insertText = "$label($CARET_POSITION)"
    insertTextFormat = InsertTextFormat.Snippet
}


private fun CompletionItem.useSourceAsDetail() {
    // https://github.com/microsoft/pyright/blob/0b7860b/packages/pyright-internal/src/languageService/completionProvider.ts#L932-L934
    detail = labelDetails.description
}


@Suppress("UnstableApiUsage")
internal class CompletionSupport(project: Project) : LspCompletionSupport() {
    
    private val configurations = project.pyrightLSConfigurations
    
    override fun createLookupElement(parameters: CompletionParameters, item: CompletionItem): LookupElement? {
        if (item.isCallable && configurations.autocompleteParentheses) {
            item.completeWithParentheses()
        }
        
        if (item.isAutoImportCompletion) {
            item.useSourceAsDetail()
        }
        
        return super.createLookupElement(parameters, item)
    }
    
}
