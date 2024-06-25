package com.insyncwithfoo.pyrightls.server

import com.insyncwithfoo.pyrightls.PyrightLSInspection
import com.insyncwithfoo.pyrightls.onlyModuleOrNull
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.insyncwithfoo.pyrightls.pyrightLSExecutable
import com.intellij.codeInspection.ex.InspectionToolRegistrar
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager


private val Project.psiManager: PsiManager
    get() = PsiManager.getInstance(this)


private val Project.pyrightLSInspectionIsEnabled: Boolean
    get() {
        val inspectionProfileManager = ProjectInspectionProfileManager.getInstance(this)
        val toolWrapper = InspectionToolRegistrar.getInstance().createTools()
            .find { it.shortName == PyrightLSInspection.SHORT_NAME } ?: return false
        
        return inspectionProfileManager.currentProfile.isToolEnabled(toolWrapper.displayKey)
    }


private val PsiElement.module: Module?
    get() = ModuleUtilCore.findModuleForPsiElement(this) ?: project.onlyModuleOrNull


@Suppress("UnstableApiUsage")
internal class PyrightLSSupportProvider : LspServerSupportProvider {
    
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        val psiFile = project.psiManager.findFile(file) ?: return
        
        val configurations = project.pyrightLSConfigurations
        val fileIsSupported = file.extension in configurations.targetedFileExtensionList
        
        if (fileIsSupported && project.pyrightLSInspectionIsEnabled) {
            val executable = project.pyrightLSExecutable ?: return
            val descriptor = PyrightLSDescriptor(project, psiFile.module, executable)
            
            serverStarter.ensureServerStarted(descriptor)
        }
    }
    
}
