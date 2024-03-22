package com.insyncwithfoo.pyrightls.configuration.project

import com.insyncwithfoo.pyrightls.configuration.common.HasConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil


@State(name = "ProjectConfigurations", storages = [Storage("pyright-langserver.xml")])
@Service(Service.Level.PROJECT)
internal class ConfigurationService :
    SimplePersistentStateComponent<Configurations>(Configurations()),
    HasConfigurations<Configurations> {
    
    override var configurations: Configurations
        get() = state
        set(value) = XmlSerializerUtil.copyBean(value, state)
    
    companion object {
        fun getInstance(project: Project): ConfigurationService = project.service()
    }
    
}
