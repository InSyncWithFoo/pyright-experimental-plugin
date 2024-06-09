package com.insyncwithfoo.pyrightls.server

import com.insyncwithfoo.pyrightls.configuration.project.WorkspaceFolders
import com.insyncwithfoo.pyrightls.message
import com.insyncwithfoo.pyrightls.path
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.insyncwithfoo.pyrightls.server.diagnostics.DiagnosticsSupport
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.modules
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerDescriptor
import java.nio.file.Path


private fun Project.getModuleSourceRoots(): Collection<VirtualFile> =
    modules.flatMap { module ->
        ModuleRootManager.getInstance(module).sourceRoots.asIterable()
    }


private fun Project.getWorkspaceFolders(type: WorkspaceFolders): Collection<VirtualFile> =
    when (type) {
        WorkspaceFolders.PROJECT_BASE -> getBaseDirectories()
        WorkspaceFolders.SOURCE_ROOTS -> getModuleSourceRoots()
    }


private fun Project.getWorkspaceFolders(): Collection<VirtualFile> =
    getWorkspaceFolders(pyrightLSConfigurations.workspaceFolders)


@Suppress("UnstableApiUsage")
internal class PyrightLSDescriptor(project: Project, private val executable: Path) :
    LspServerDescriptor(project, PRESENTABLE_NAME, *project.getWorkspaceFolders().toTypedArray()) {
    
    private val configurations = project.pyrightLSConfigurations
    
    override val lspServerListener = Listener(project)
    
    override val lspGoToDefinitionSupport = configurations.goToDefinitionSupport
    override val lspHoverSupport = configurations.hoverSupport
    override val lspCompletionSupport = CompletionSupport(project).takeIf { configurations.completionSupport }
    override val lspDiagnosticsSupport = DiagnosticsSupport(project).takeIf { configurations.diagnosticsSupport }
    
    init {
        LOGGER.info(configurations.toString())
    }
    
    override fun isSupportedFile(file: VirtualFile) =
        file.extension in configurations.targetedFileExtensionList
    
    override fun createCommandLine() =
        GeneralCommandLine(executable.toString(), "--stdio").apply {
            withWorkDirectory(project.path?.toString())
            withCharset(Charsets.UTF_8)
        }
    
    companion object {
        val PRESENTABLE_NAME = message("languageServer.representableName")
        private val LOGGER = Logger.getInstance(PyrightLSDescriptor::class.java)
    }
    
}
