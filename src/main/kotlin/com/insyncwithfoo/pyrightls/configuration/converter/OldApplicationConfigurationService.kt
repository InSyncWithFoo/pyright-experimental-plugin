package com.insyncwithfoo.pyrightls.configuration.converter

import com.insyncwithfoo.pyrightls.configuration.application.Configurations
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service


@State(
    name = "ApplicationConfigurations",
    storages = [Storage("pyright-langserver.xml", roamingType = RoamingType.LOCAL)]
)
@Service(Service.Level.APP)
internal class OldApplicationConfigurationService : SimplePersistentStateComponent<Configurations>(Configurations()) {
    companion object {
        fun getInstance() = service<OldApplicationConfigurationService>()
    }
}
