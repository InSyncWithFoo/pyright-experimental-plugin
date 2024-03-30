package com.insyncwithfoo.pyrightls

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import java.nio.file.Path


@Suppress("UnstableApiUsage")
internal class PyrightLSLspServerDescriptor(
    project: Project,
    private val executable: Path
) : ProjectWideLspServerDescriptor(project, PRESENTABLE_NAME) {
    
    override fun isSupportedFile(file: VirtualFile) = file.extension == "py"
    
    override fun createCommandLine() =
        GeneralCommandLine(executable.toString(), "--stdio").apply {
            withCharset(Charsets.UTF_8)
        }
    
    override val lspServerListener = PyrightLSLspServerListener(project)
    
    companion object {
        val PRESENTABLE_NAME = message("languageServer.representableName")
    }
    
}
