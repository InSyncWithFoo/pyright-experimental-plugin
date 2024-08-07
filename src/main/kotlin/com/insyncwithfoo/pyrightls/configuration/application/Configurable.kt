package com.insyncwithfoo.pyrightls.configuration.application

import com.insyncwithfoo.pyrightls.configuration.PyrightLSConfigurable
import com.insyncwithfoo.pyrightls.configuration.copy
import com.insyncwithfoo.pyrightls.isNormal
import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager


private val ProjectManager.undisposedProjects: Sequence<Project>
    get() = openProjects.asSequence().filter { it.isNormal }


internal class Configurable : PyrightLSConfigurable<Configurations>() {
    
    override val service = ConfigurationService.getInstance()
    override val state = service.state.copy()
    
    override val panel by lazy { configurationPanel(state) }
    
    override fun getDisplayName() = message("configurations.global.displayName")
    
    override fun apply() {
        super.apply()
        restartAllPyrightServersIfSoChoose()
    }
    
    private fun restartAllPyrightServersIfSoChoose() {
        ProjectManager.getInstance().undisposedProjects.forEach { project ->
            project.restartPyrightServersIfSoChoose()
        }
    }
    
}
