package com.insyncwithfoo.pyrightls.configuration.converter

import com.insyncwithfoo.pyrightls.configuration.copy
import com.intellij.ide.AppLifecycleListener
import com.intellij.ide.util.RunOnceUtil
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.insyncwithfoo.pyrightls.configuration.application.ConfigurationService as ApplicationConfigurationService
import com.insyncwithfoo.pyrightls.configuration.project.ConfigurationService as ProjectConfigurationService


internal class ConfigurationsConverter : AppLifecycleListener, ProjectActivity {
    
    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        val id = "com.insyncwithfoo.pyrightls.configuration.converter.ApplicationConfigurationsConverter"
        
        RunOnceUtil.runOnceForApp(id) {
            val oldService = OldApplicationConfigurationService.getInstance()
            val newService = ApplicationConfigurationService.getInstance()
            
            migrate(oldService, newService)
        }
    }
    
    private fun projectOpened(project: Project) {
        val id = "com.insyncwithfoo.pyrightls.configuration.converter.ProjectConfigurationsConverter"
        
        RunOnceUtil.runOnceForProject(project, id) {
            val oldService = OldProjectConfigurationService.getInstance(project)
            val newService = ProjectConfigurationService.getInstance(project)
            
            migrate(oldService, newService)
        }
    }
    
    override suspend fun execute(project: Project) {
        projectOpened(project)
    }
    
    private inline fun <reified C : BaseState> migrate(
        oldService: PersistentStateComponent<C>,
        newService: PersistentStateComponent<C>
    ) {
        LOGGER.info("Migrating service names. Old service state: [${oldService.state}]")
        
        newService.loadState(oldService.state!!.copy())
        // oldService.loadState(C::class.constructors.first().call())
    }
    
    companion object {
        private val LOGGER = Logger.getInstance(ConfigurationsConverter::class.java)
    }
    
}
