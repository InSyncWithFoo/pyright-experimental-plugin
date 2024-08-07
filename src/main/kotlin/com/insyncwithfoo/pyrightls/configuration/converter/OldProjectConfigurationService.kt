package com.insyncwithfoo.pyrightls.configuration.converter

import com.insyncwithfoo.pyrightls.configuration.project.Configurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project


@State(name = "ProjectConfigurations", storages = [Storage("pyright-langserver.xml")])
@Service(Service.Level.PROJECT)
internal class OldProjectConfigurationService : SimplePersistentStateComponent<Configurations>(Configurations()) {
    companion object {
        fun getInstance(project: Project) = project.service<OldProjectConfigurationService>()
    }
}
