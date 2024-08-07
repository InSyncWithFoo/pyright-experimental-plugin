package com.insyncwithfoo.pyrightls.configuration.project

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project


@State(
    name = "com.insyncwithfoo.pyrightls.configuration.project.ConfigurationService",
    storages = [Storage("pyright-langserver.xml")]
)
@Service(Service.Level.PROJECT)
internal class ConfigurationService : SimplePersistentStateComponent<Configurations>(Configurations()) {
    companion object {
        fun getInstance(project: Project) = project.service<ConfigurationService>()
    }
}
