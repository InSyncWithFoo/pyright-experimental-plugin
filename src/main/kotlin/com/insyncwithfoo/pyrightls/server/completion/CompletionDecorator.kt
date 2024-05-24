package com.insyncwithfoo.pyrightls.server.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.LogicalPosition
import org.eclipse.lsp4j.InsertReplaceEdit
import org.eclipse.lsp4j.Position
import org.eclipse.lsp4j.Range
import org.eclipse.lsp4j.TextEdit
import org.eclipse.lsp4j.jsonrpc.messages.Either


private typealias LookupElementWrapper = LookupElementDecorator<LookupElementBuilder>


private fun Document.getLogicalPosition(offset: Int): LogicalPosition {
    val line = getLineNumber(offset)
    val column = offset - getLineStartOffset(line)
    
    return LogicalPosition(line, column)
}


private fun Document.getOffset(position: Position): Int {
    val lineStart = getLineStartOffset(position.line)
    return lineStart + position.character
}


private fun Document.replaceString(range: Range, replacement: String) {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    replaceString(start, end, replacement)
}


internal class CompletionDecorator(
    private val builder: LookupElementBuilder,
    private val itemTextEdit: Either<TextEdit, InsertReplaceEdit>?
) : LookupElementWrapper(builder) {
    
    override fun getDelegateInsertHandler() = InsertHandler<LookupElementBuilder> { context, _ ->
        val (range, replacement) = when (val edit = itemTextEdit?.get()) {
            is TextEdit -> edit.range to edit.newText
            is InsertReplaceEdit -> (edit.replace ?: edit.insert) to edit.newText
            else -> {
                builder.handleInsert(context)
                return@InsertHandler
            }
        }
        
        val document = context.document
        document.replaceString(range, replacement)
        
        val newCaretOffset = document.getOffset(range.start) + replacement.length
        val newCaretPosition = document.getLogicalPosition(newCaretOffset)
        
        context.editor.caretModel.moveToLogicalPosition(newCaretPosition)
    }
    
}
