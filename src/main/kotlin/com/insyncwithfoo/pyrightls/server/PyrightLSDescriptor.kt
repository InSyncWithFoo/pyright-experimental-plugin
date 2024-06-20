package com.insyncwithfoo.pyrightls.server

import com.insyncwithfoo.pyrightls.configuration.project.WorkspaceFolders
import com.insyncwithfoo.pyrightls.message
import com.insyncwithfoo.pyrightls.path
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.insyncwithfoo.pyrightls.server.diagnostics.DiagnosticsSupport
import com.insyncwithfoo.pyrightls.wslDistribution
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.wsl.WSLCommandLineOptions
import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.WslPath
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.modules
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.io.OSAgnosticPathUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerDescriptor
import java.net.URI
import java.nio.file.Path


private val Path.isUncPath: Boolean
    get() = WslPath.parseWindowsUncPath(this.toString()) != null


private val URI.pathIsAbsolute: Boolean
    get() = OSAgnosticPathUtil.isAbsoluteDosPath(Path.of(this).toString())


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
    
    override fun createInitializeParams() = super.createInitializeParams().apply {
        val wslDistribution = project.wslDistribution
        
        @Suppress("DEPRECATION")
        if (wslDistribution != null) {
            rootUri = null
            rootPath = null
        }
    }
    
    override fun getFileUri(file: VirtualFile): String {
        val wslDistribution = project.wslDistribution
        
        return when {
            wslDistribution == null -> super.getFileUri(file)
            else -> wslDistribution.getWslPath(Path.of(file.path))!!
        }
    }
    
    override fun findFileByUri(fileUri: String) =
        findFileByUri(URI.create(fileUri))
    
    private fun findFileByUri(fileUri: URI): VirtualFile? {
        val wslDistribution = project.wslDistribution
        
        val virtualFileUri = when {
            wslDistribution == null || fileUri.pathIsAbsolute -> fileUri
            else -> Path.of(wslDistribution.getWindowsPath(fileUri.path)).toUri()
        }
        
        return super.findFileByUri(virtualFileUri.toString())
    }
    
    override fun createCommandLine() = GeneralCommandLine().apply {
        val wslDistribution = project.wslDistribution
        val projectPath = project.path
        val exePath = wslDistribution.getPureLinuxOrWindowsPath(executable)
        
        withExePath(exePath)
        addParameter("--stdio")
        
        withCharset(Charsets.UTF_8)
        withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        
        if (projectPath != null) {
            withWorkDirectory(projectPath.toString())
        }
        
        wslDistribution?.patchCommandLine(this, project, WSLCommandLineOptions())
    }
    
    private fun WSLDistribution?.getPureLinuxOrWindowsPath(path: Path) = when {
        this != null && path.isUncPath -> this.getWslPath(path)!!
        else -> path.toString()
    }
    
    companion object {
        val PRESENTABLE_NAME = message("languageServer.representableName")
        private val LOGGER = Logger.getInstance(PyrightLSDescriptor::class.java)
    }
    
}
