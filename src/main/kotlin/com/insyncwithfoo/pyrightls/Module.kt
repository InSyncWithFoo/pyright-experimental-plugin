package com.insyncwithfoo.pyrightls

import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.target.WslTargetEnvironmentConfiguration
import com.intellij.openapi.module.Module
import com.intellij.openapi.projectRoots.Sdk
import com.jetbrains.python.sdk.PythonSdkUtil
import com.jetbrains.python.target.PyTargetAwareAdditionalData
import java.nio.file.Path


private val Module?.sdk: Sdk?
    get() = PythonSdkUtil.findPythonSdk(this)


internal val Sdk.wslDistribution: WSLDistribution?
    get() {
        val additionalData = sdkAdditionalData as? PyTargetAwareAdditionalData ?: return null
        val configuration = additionalData.targetEnvironmentConfiguration as? WslTargetEnvironmentConfiguration
        
        return configuration?.distribution
    }


internal val Module.interpreterPath: Path?
    get() = sdk?.homePath?.toPathOrNull() ?: project.interpreterPath


internal val Module.wslDistribution: WSLDistribution?
    get() = sdk?.wslDistribution ?: project.wslDistribution
