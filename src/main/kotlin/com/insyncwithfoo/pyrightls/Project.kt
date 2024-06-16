package com.insyncwithfoo.pyrightls

import com.insyncwithfoo.pyrightls.configuration.AllConfigurations
import com.insyncwithfoo.pyrightls.configuration.ConfigurationService
import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.target.WslTargetEnvironmentConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.jetbrains.python.target.PyTargetAwareAdditionalData
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries


private val Project.sdk: Sdk?
    get() = ProjectRootManager.getInstance(this).projectSdk


internal val Project.wslDistribution: WSLDistribution?
    get() {
        val sdk = this.sdk ?: return null
        val additionalData = sdk.sdkAdditionalData as? PyTargetAwareAdditionalData ?: return null
        val configuration = additionalData.targetEnvironmentConfiguration as? WslTargetEnvironmentConfiguration
        
        return configuration?.distribution
    }


internal val Project.path: Path?
    get() = basePath?.let { Path.of(it) }


internal val Project.sdkPath: Path?
    get() = sdk?.homePath?.let { Path.of(it) }


internal val Project.isNormal: Boolean
    get() = !this.isDefault && !this.isDisposed


internal val Project.pyrightLSConfigurations: AllConfigurations
    get() = ConfigurationService.getInstance(this).state


internal val Project.pyrightLSExecutable: Path?
    get() = pyrightLSConfigurations.executable?.toPathIfItExists(base = this.path)


internal fun Project.isPyrightLSInspectionEnabled(): Boolean {
    val inspectionManager = InspectionProjectProfileManager.getInstance(this)
    val profile = inspectionManager.currentProfile
    val pyrightLSInspection = profile.allTools.find { it.tool.shortName == PyrightLSInspection.SHORT_NAME }
    
    return pyrightLSInspection?.isEnabled ?: false
}


internal fun Project.findPyrightLSExecutable(): Path? {
    val sdkDirectory = sdkPath?.parent ?: return null
    val children = sdkDirectory.listDirectoryEntries()
    
    return children.find { it.isProbablyPyrightLSExecutable }
}
