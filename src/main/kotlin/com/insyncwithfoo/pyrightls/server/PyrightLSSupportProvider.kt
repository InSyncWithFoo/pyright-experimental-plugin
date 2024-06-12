package com.insyncwithfoo.pyrightls.server

import com.insyncwithfoo.pyrightls.PyrightLSInspection
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.insyncwithfoo.pyrightls.pyrightLSExecutable
import com.intellij.codeInspection.ex.InspectionToolRegistrar
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager


private val Project.pyrightLSInspectionIsEnabled: Boolean
    get() {
        val inspectionProfileManager = ProjectInspectionProfileManager.getInstance(this)
        val toolWrapper = InspectionToolRegistrar.getInstance().createTools()
            .find { it.shortName == PyrightLSInspection.SHORT_NAME } ?: return false
        
        return inspectionProfileManager.currentProfile.isToolEnabled(toolWrapper.displayKey)
    }


@Suppress("UnstableApiUsage")
internal class PyrightLSSupportProvider : LspServerSupportProvider {
    
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        val fileIsSupported = file.extension in project.pyrightLSConfigurations.targetedFileExtensionList
        
        if (fileIsSupported && project.pyrightLSInspectionIsEnabled) {
            val executable = project.pyrightLSExecutable ?: return
            val descriptor = PyrightLSDescriptor(project, executable)
            
            serverStarter.ensureServerStarted(descriptor)
        }
    }
    
}
