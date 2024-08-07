package com.insyncwithfoo.pyrightls.server

import com.insyncwithfoo.pyrightls.interpreterPath
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.insyncwithfoo.pyrightls.wslDistribution
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerListener
import com.intellij.platform.lsp.api.LspServerManager
import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.InitializeResult


private val Module.osDependentInterpreterPath: String?
    get() {
        val interpreterPath = this.interpreterPath?.toString()
        
        return when (wslDistribution) {
            null -> interpreterPath
            else -> interpreterPath?.replace("\\", "/")
        }
    }


private fun Project.createPyrightLSSettingsObject(module: Module? = null) = Settings().apply {
    val configurations = pyrightLSConfigurations
    
    python {
        pythonPath = module?.osDependentInterpreterPath
        
        analysis {
            logLevel = configurations.logLevel.label
            autoImportCompletions = configurations.autoImportCompletions
            diagnosticMode = configurations.diagnosticMode.value
            autoSearchPaths = configurations.autoSearchPaths
        }
    }
    
    pyright {
        disableTaggedHints = !configurations.taggedHints
    }
}


@Suppress("UnstableApiUsage")
internal class Listener(val project: Project, private val module: Module? = null) : LspServerListener {
    
    override fun serverInitialized(params: InitializeResult) {
        val lspServerManager = LspServerManager.getInstance(project)
        val settings = project.createPyrightLSSettingsObject(module)
        val parameters = DidChangeConfigurationParams(settings)
        
        lspServerManager.getServersForProvider(PyrightLSSupportProvider::class.java).forEach { lspServer ->
            lspServer.sendNotification {
                it.workspaceService.didChangeConfiguration(parameters)
            }
        }
    }
    
}
