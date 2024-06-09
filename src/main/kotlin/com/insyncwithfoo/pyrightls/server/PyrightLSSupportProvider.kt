package com.insyncwithfoo.pyrightls.server

import com.insyncwithfoo.pyrightls.PyrightLSInspection
import com.insyncwithfoo.pyrightls.configuration.ConfigurationService
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.intellij.codeInspection.ex.InspectionToolRegistrar
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import java.nio.file.Path
import kotlin.io.path.exists


private val Project.isPyrightLSEnabled: Boolean
    get() {
        val inspectionProfileManager = ProjectInspectionProfileManager.getInstance(this)
        val toolWrapper = InspectionToolRegistrar.getInstance().createTools()
            .find { it.shortName == PyrightLSInspection.SHORT_NAME } ?: return false
        
        return inspectionProfileManager.currentProfile.isToolEnabled(toolWrapper.displayKey)
    }


private val Project.pyrightLSExecutable: Path?
    get() {
        val projectPath = Path.of(basePath ?: "")
        
        val configurationService = ConfigurationService.getInstance(this)
        val configurations = configurationService.state
        
        return projectPath.resolve(configurations.executable ?: return null).normalize()
    }


@Suppress("UnstableApiUsage")
internal class PyrightLSSupportProvider : LspServerSupportProvider {
    
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        val fileIsSupported = file.extension in project.pyrightLSConfigurations.targetedFileExtensionList
        
        if (fileIsSupported && project.isPyrightLSEnabled) {
            val executable = project.pyrightLSExecutable?.takeIf { it.exists() } ?: return
            val descriptor = PyrightLSDescriptor(project, executable)
            
            serverStarter.ensureServerStarted(descriptor)
        }
    }
    
}
