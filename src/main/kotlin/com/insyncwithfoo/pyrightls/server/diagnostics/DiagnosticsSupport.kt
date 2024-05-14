package com.insyncwithfoo.pyrightls.server.diagnostics

import com.insyncwithfoo.pyrightls.HighlightSeverity
import com.insyncwithfoo.pyrightls.PyrightLSInspection
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import org.eclipse.lsp4j.Diagnostic
import org.eclipse.lsp4j.DiagnosticSeverity


private fun <T> T.letIf(condition: Boolean, block: (T) -> T): T =
    if (condition) block(this) else this


private fun <T> T.runIf(condition: Boolean, block: T.() -> T): T =
    if (condition) block() else this


private fun HtmlChunk.Element.withFont(font: String?) =
    this.runIf(font != null) { style("font-family: '$font'") }


private fun String.toPreformattedBlock(font: String?) =
    HtmlChunk.div().withFont(font).child(HtmlChunk.text(this))


private fun String.toCodeSuffix(font: String? = null, href: String? = null): String {
    val parenthesizedPortion = href?.let { HtmlChunk.link(it, this).withFont(font) } ?: this
    return " ($parenthesizedPortion)"
}


private val Diagnostic.codeAsString: String?
    get() = code?.get() as String?


private val Diagnostic.rawCodeSuffix: String
    get() = codeAsString?.toCodeSuffix().orEmpty()


private val Diagnostic.suffixedMessage: String
    get() = "$message$rawCodeSuffix"


private val Diagnostic.isUnsuppressable: Boolean
    get() {
        val unsuppressableErrorCodes = listOf("reportUnnecessaryTypeIgnoreComment")
        
        return codeAsString in unsuppressableErrorCodes
    }

private fun Project.getPyrightLSInspection(): PyrightLSInspection {
    val inspectionManager = InspectionProjectProfileManager.getInstance(this)
    val profile = inspectionManager.currentProfile
    
    return profile.getInspectionTool(PyrightLSInspection.SHORT_NAME, this)!!.tool as PyrightLSInspection
}


@Suppress("UnstableApiUsage")
internal class DiagnosticsSupport(private val project: Project) : LspDiagnosticsSupport() {
    
    override fun getHighlightSeverity(diagnostic: Diagnostic): HighlightSeverity? {
        val inspection = project.getPyrightLSInspection()
        
        return when (diagnostic.severity) {
            DiagnosticSeverity.Error -> HighlightSeverity(inspection.highlightSeverityForErrors)
            DiagnosticSeverity.Warning -> HighlightSeverity(inspection.highlightSeverityForWarnings)
            DiagnosticSeverity.Information -> HighlightSeverity(inspection.highlightSeverityForInformation)
            DiagnosticSeverity.Hint -> super.getHighlightSeverity(diagnostic)
            else -> throw RuntimeException("This should not happen")
        }
    }
    
    override fun getMessage(diagnostic: Diagnostic) = diagnostic.suffixedMessage
    
    override fun getTooltip(diagnostic: Diagnostic): String {
        val configurations = project.pyrightLSConfigurations
        
        val descriptionHref = diagnostic.codeDescription?.href
            ?.takeIf { configurations.linkErrorCodes }
        val font = EditorUtil.getEditorFont().name
            ?.takeIf { configurations.useEditorFont }
        val tooltip = diagnostic.message
            .letIf(configurations.addTooltipPrefix) { "Pyright: $it" }
        
        val codeSuffix = diagnostic.codeAsString?.toCodeSuffix(font, descriptionHref).orEmpty()
        
        return tooltip.toPreformattedBlock(font).addRaw(codeSuffix).toString()
    }
    
    override fun createAnnotation(
        holder: AnnotationHolder,
        diagnostic: Diagnostic,
        textRange: TextRange,
        quickFixes: List<IntentionAction>
    ) {
        val fix = when {
            diagnostic.isUnsuppressable -> null
            else -> SuppressQuickFix(diagnostic.codeAsString, textRange)
        }
        val newQuickFixes = quickFixes + listOfNotNull(fix)
        
        super.createAnnotation(holder, diagnostic, textRange, newQuickFixes)
    }
    
}
