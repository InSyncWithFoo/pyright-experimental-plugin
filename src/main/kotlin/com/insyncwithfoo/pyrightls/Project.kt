package com.insyncwithfoo.pyrightls

import com.insyncwithfoo.pyrightls.configuration.AllConfigurations
import com.insyncwithfoo.pyrightls.configuration.ConfigurationService
import com.intellij.execution.wsl.WSLDistribution
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries


private val <T> Array<T>.onlyElement: T?
    get() = firstOrNull().takeIf { size == 1 }


private val Project.modules: Array<Module>
    get() = ModuleManager.getInstance(this).modules


private val Project.sdk: Sdk?
    get() = ProjectRootManager.getInstance(this).projectSdk


internal val Project.wslDistribution: WSLDistribution?
    get() = sdk?.wslDistribution


internal val Project.path: Path?
    get() = basePath?.let { Path.of(it) }


internal val Project.interpreterPath: Path?
    get() = sdk?.homePath?.let { Path.of(it) }


internal val Project.isNormal: Boolean
    get() = !this.isDefault && !this.isDisposed


internal val Project.onlyModuleOrNull: Module?
    get() = modules.onlyElement


internal val Project.hasOnlyOneModule: Boolean
    get() = modules.size == 1


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
    val interpreterDirectory = interpreterPath?.parent ?: return null
    val children = interpreterDirectory.listDirectoryEntries()
    
    return children.find { it.isProbablyPyrightLSExecutable }
}
