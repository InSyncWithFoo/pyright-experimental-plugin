package com.insyncwithfoo.pyrightls.configuration.application

import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service


@State(
    name = "ApplicationConfigurations",
    storages = [Storage("pyright-langserver.xml", roamingType = RoamingType.DISABLED)]
)
@Service(Service.Level.APP)
internal class ConfigurationService : SimplePersistentStateComponent<Configurations>(Configurations()) {
    companion object {
        fun getInstance() = service<ConfigurationService>()
    }
}
