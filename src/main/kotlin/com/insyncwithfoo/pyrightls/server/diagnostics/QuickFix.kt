package com.insyncwithfoo.pyrightls.server.diagnostics

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyUtil
import com.jetbrains.python.psi.impl.PyPsiUtils


internal typealias PyrightErrorCode = String


private fun Document.appendToLine(line: Int, value: String) {
    val lineEndOffset = getLineEndOffset(line)
    replaceString(lineEndOffset, lineEndOffset, value)
}


private fun PsiFile.findSameLineComment(rangeOffset: Int): PsiComment? {
    return findElementAt(rangeOffset)?.let { PyPsiUtils.findSameLineComment(it) }
}


private fun PsiFile.edit(callback: (Document) -> Unit) {
    PyUtil.updateDocumentUnblockedAndCommitted(this, callback)
}


internal class SuppressQuickFix(
    private val code: PyrightErrorCode?,
    private val range: TextRange
) : IntentionAction {
    
    private fun Document.appendNewCommentToLine() {
        val lineNumber = getLineNumber(range.startOffset)
        val lastCharacter = charsSequence[getLineEndOffset(lineNumber) - 1]
        
        val padding = when {
            lastCharacter == ' ' -> ""
            else -> "  "
        }
        
        appendToLine(lineNumber, padding + PyrightIgnoreComment.parse(code ?: ""))
    }
    
    private fun Document.removeCommentCodeList(comment: ExistingPyrightIgnoreComment) {
        val (start, end) = comment.codeListRemovalOffsets
        replaceString(start, end, "")
    }
    
    private fun Document.addNewCodeToExistingComment(comment: ExistingPyrightIgnoreComment) {
        val (start, end) = comment.codeListReplacementOffsets
        replaceString(start, end, comment.replacement)
    }
    
    private val ExistingPyrightIgnoreComment.replacement: String
        get() {
            val newCodes = codes + code!!
            val newComment = PyrightIgnoreComment(newCodes)
            
            val padding = when {
                codeListIsNotSpecified -> " "
                else -> ""
            }
            
            return padding + newComment.codeList
        }
    
    override fun startInWriteAction() = true
    
    override fun getFamilyName() = "Suppress Pyright diagnostics"
    
    override fun getText() = "Suppress ${code ?: "this Pyright diagnostic"}"
    
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        val offset = editor?.caretModel?.offset ?: return false
        return range.startOffset <= offset && offset <= range.endOffset
    }
    
    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) {
            return
        }
        
        val existingComment = file.findSameLineComment(range.startOffset)
            ?.let { ExistingPyrightIgnoreComment.create(it.text, it.textOffset) }
        
        when {
            existingComment == null -> file.edit { it.appendNewCommentToLine() }
            code == null -> file.edit { it.removeCommentCodeList(existingComment) }
            else -> file.edit { it.addNewCodeToExistingComment(existingComment) }
        }
        
    }
    
}
