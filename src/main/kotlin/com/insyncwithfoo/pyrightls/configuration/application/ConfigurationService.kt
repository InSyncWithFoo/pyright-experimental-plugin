package com.insyncwithfoo.pyrightls.configuration.application

import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service


@State(
    name = "com.insyncwithfoo.pyrightls.configuration.application.ConfigurationService",
    storages = [Storage("pyright-langserver.xml", roamingType = RoamingType.LOCAL)]
)
@Service(Service.Level.APP)
internal class ConfigurationService : SimplePersistentStateComponent<Configurations>(Configurations()) {
    companion object {
        fun getInstance() = service<ConfigurationService>()
    }
}
