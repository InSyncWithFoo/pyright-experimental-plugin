package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.configuration.PyrightLSConfigurable
import com.insyncwithfoo.pyrightls.configuration.copy
import com.insyncwithfoo.pyrightls.message
import com.intellij.openapi.project.Project


internal class Configurable(internal val project: Project) : PyrightLSConfigurable<Configurations>() {

    override val service = ConfigurationService.getInstance(project)
    override val state = service.state.copy()

    override val panel by lazy { configurationPanel(state) }

    override fun getDisplayName() = message("configurations.project.displayName")
    
    override fun apply() {
        super.apply()
        project.restartPyrightServersIfSoChoose()
    }

}
